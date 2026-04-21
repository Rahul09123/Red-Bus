# Ansible Automation for Ticketing System Deployment

This directory contains Ansible playbooks and roles for automating the deployment of the Ticketing System microservices architecture to Kubernetes.

## Directory Structure

```
ansible/
├── deploy-ticketing-system.yml    # Main playbook for deployment
├── roles/
│   ├── cluster-setup/              # Kubernetes cluster setup
│   ├── database-setup/             # PostgreSQL and pgAdmin setup
│   ├── microservices-deploy/       # Microservices deployment
│   ├── monitoring-setup/           # Prometheus and Grafana setup
│   └── cleanup/                    # Cleanup and resource removal
└── README.md                       # This file
```

## Prerequisites

Before using these Ansible playbooks, ensure the following are installed and configured:

1. **Ansible** (v2.9+)
   ```bash
   pip install ansible
   ```

2. **Kubernetes CLI (kubectl)**
   - Download from: https://kubernetes.io/docs/tasks/tools/
   - Verify: `kubectl version --client`

3. **Kubernetes Cluster**
   - Docker Desktop with Kubernetes enabled, OR
   - Minikube, OR
   - Any Kubernetes cluster (AKS, EKS, GKE, etc.)

4. **Kubernetes Python Client**
   ```bash
   pip install kubernetes
   ```

5. **Kubeconfig**
   - Ensure `~/.kube/config` is properly configured
   - Verify: `kubectl cluster-info`

6. **Kubernetes Manifests**
   - All YAML files referenced in the roles must exist in the `k8s/` directory

## Installation

1. Install required Python packages:
   ```bash
   pip install -r requirements.txt
   ```

2. Verify Ansible installation:
   ```bash
   ansible --version
   ```

3. Verify Kubernetes access:
   ```bash
   kubectl cluster-info
   kubectl get nodes
   ```

## Configuration

### Default Variables

All roles use default variables defined in `roles/<role-name>/defaults/main.yml`:

- **Namespace**: `ticketing-system` (change in defaults)
- **Kubeconfig Path**: `~/.kube/config`
- **Postgres Password**: `123` (CHANGE in production!)
- **Replica Count**: 1 (increase for HA)

### Modifying Configuration

To override defaults, create `group_vars/all.yml` or use `-e` flag:

```bash
ansible-playbook deploy-ticketing-system.yml -e "k8s_namespace=production"
```

## Usage

### Full Deployment

Deploy the entire system:
```bash
ansible-playbook deploy-ticketing-system.yml
```

### Selective Deployment

Deploy specific components using tags:

```bash
# Cluster setup only
ansible-playbook deploy-ticketing-system.yml --tags setup

# Database setup only
ansible-playbook deploy-ticketing-system.yml --tags database

# Deploy microservices only
ansible-playbook deploy-ticketing-system.yml --tags services

# Setup monitoring only
ansible-playbook deploy-ticketing-system.yml --tags monitoring

# Multiple tags
ansible-playbook deploy-ticketing-system.yml --tags services,monitoring
```

### Cleanup

Remove all deployed resources:
```bash
ansible-playbook deploy-ticketing-system.yml -e "perform_cleanup=true"
```

Delete entire namespace:
```bash
ansible-playbook deploy-ticketing-system.yml -e "perform_cleanup=true" -e "cleanup_namespace=true"
```

## Role Descriptions

### cluster-setup
- Creates Kubernetes namespace
- Sets up network policies
- Configures RBAC (if needed)
- **Duration**: ~2 minutes

### database-setup
- Deploys PostgreSQL to Kubernetes
- Sets up pgAdmin for database management
- Initializes four databases
- Waits for PostgreSQL readiness
- **Duration**: ~5-10 minutes

### microservices-deploy
- Deploys Eureka Service Registry
- Deploys API Gateway
- Deploys all microservices (Member, Security, Expedition, Payment)
- Deploys Frontend application
- Waits for all services to be ready
- **Duration**: ~10-15 minutes

### monitoring-setup
- Deploys Prometheus for metrics collection
- Deploys Grafana for visualization
- Configures Prometheus scrape configs
- **Duration**: ~5 minutes

### cleanup
- Removes all Kubernetes resources
- Optional full namespace deletion
- **Duration**: ~2 minutes

## Monitoring

### Access Grafana Dashboard
```bash
kubectl port-forward -n ticketing-system svc/grafana 3000:3000
# Open http://localhost:3000
# Default credentials: admin/admin
```

### Access pgAdmin
```bash
kubectl port-forward -n ticketing-system svc/pgadmin 5050:80
# Open http://localhost:5050
```

### Access API Gateway
```bash
kubectl port-forward -n ticketing-system svc/api-gateway 8080:8080
# API available at http://localhost:8080
```

### View Logs
```bash
# View specific pod logs
kubectl logs -n ticketing-system <pod-name>

# View logs with follow
kubectl logs -f -n ticketing-system <pod-name>

# View logs from specific service
kubectl logs -n ticketing-system -l app=member-service
```

### Check Pod Status
```bash
# All pods in namespace
kubectl get pods -n ticketing-system

# Watch pod status
kubectl get pods -n ticketing-system -w

# Get detailed pod information
kubectl describe pod -n ticketing-system <pod-name>
```

## Troubleshooting

### Pods Not Starting
```bash
# Check pod status
kubectl describe pod -n ticketing-system <pod-name>

# Check pod logs
kubectl logs -n ticketing-system <pod-name>

# Check events
kubectl get events -n ticketing-system
```

### Database Connection Issues
1. Verify PostgreSQL is running:
   ```bash
   kubectl get pods -n ticketing-system -l app=postgres
   ```
2. Check database initialization:
   ```bash
   kubectl logs -n ticketing-system <postgres-pod-name>
   ```
3. Verify services are exposed:
   ```bash
   kubectl get svc -n ticketing-system
   ```

### Image Pull Errors
- Ensure Docker images are built and pushed to registry
- Check registry availability
- Verify image names in YAML files

### Resource Limits
- Check node resources: `kubectl top nodes`
- Check pod resource usage: `kubectl top pods -n ticketing-system`
- Increase cluster resources if needed

## Best Practices

1. **Pre-deployment Checks**
   ```bash
   kubectl cluster-info
   kubectl get nodes
   ```

2. **Use Separate Namespaces**
   - Development: `ticketing-system-dev`
   - Staging: `ticketing-system-staging`
   - Production: `ticketing-system-prod`

3. **Resource Management**
   - Set appropriate resource requests and limits
   - Monitor cluster capacity
   - Plan for growth

4. **Security**
   - Change default database password
   - Use secrets for sensitive data
   - Implement RBAC policies
   - Use network policies

5. **Monitoring**
   - Enable comprehensive logging
   - Set up alerts in Grafana
   - Monitor resource usage

## Advanced Usage

### Custom Variables File
Create `group_vars/all.yml`:
```yaml
k8s_namespace: ticketing-system
docker_registry: myregistry.azurecr.io
replica_count: 3
```

Run with variables:
```bash
ansible-playbook deploy-ticketing-system.yml
```

### Dry Run (Check Mode)
```bash
ansible-playbook deploy-ticketing-system.yml --check
```

### Verbose Output
```bash
ansible-playbook deploy-ticketing-system.yml -v      # Basic
ansible-playbook deploy-ticketing-system.yml -vv     # More verbose
ansible-playbook deploy-ticketing-system.yml -vvv    # Very verbose
```

### Limit to Specific Hosts
```bash
ansible-playbook deploy-ticketing-system.yml --limit localhost
```

## Performance Optimization

1. **Parallel Execution**
   - Ansible processes multiple tasks concurrently
   - Adjust forks in `ansible.cfg` if needed

2. **Retry Policies**
   - Database waits use exponential backoff
   - Services wait for pod readiness
   - Configurable retry counts and delays

3. **Caching**
   - Enable fact caching in `ansible.cfg`
   - Reduces repeated queries

## CI/CD Integration

### GitHub Actions Example
```yaml
- name: Deploy with Ansible
  run: |
    ansible-playbook deploy-ticketing-system.yml \
      -e "k8s_namespace=production" \
      -e "replica_count=3"
```

### Jenkins Pipeline Example
```groovy
stage('Deploy') {
  steps {
    sh '''
      ansible-playbook deploy-ticketing-system.yml \
        -e "k8s_namespace=production" \
        -i inventory.yml
    '''
  }
}
```

## Scaling

### Scale Deployment
```bash
kubectl scale deployment -n ticketing-system <service-name> --replicas=3
```

### Update Deployment Count
Modify `replica_count` in `group_vars/all.yml` and redeploy specific services.

## Backup and Recovery

### Backup Database
```bash
kubectl exec -n ticketing-system <postgres-pod> -- pg_dump -U postgres expeditionDB > backup.sql
```

### Restore Database
```bash
kubectl exec -i -n ticketing-system <postgres-pod> -- psql -U postgres < backup.sql
```

## Support and Debugging

### Get Full Deployment Status
```bash
ansible-playbook deploy-ticketing-system.yml --check -v
```

### Validate YAML Syntax
```bash
ansible-playbook deploy-ticketing-system.yml --syntax-check
```

### Check Ansible Inventory
```bash
ansible-inventory --list
```

## Additional Resources

- [Ansible Documentation](https://docs.ansible.com/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [Ansible Kubernetes Module](https://docs.ansible.com/ansible/latest/collections/kubernetes/core/k8s_module.html)

## License

Same as the main project

## Contact

For questions or issues, contact the DevOps team.
