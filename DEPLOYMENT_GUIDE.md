# Ticketing System Deployment Guide

This guide provides step-by-step instructions for deploying the Ticketing System using Ansible.

## Table of Contents

1. [Quick Start](#quick-start)
2. [Prerequisites Verification](#prerequisites-verification)
3. [Initial Setup](#initial-setup)
4. [Deployment Scenarios](#deployment-scenarios)
5. [Post-Deployment Validation](#post-deployment-validation)
6. [Troubleshooting](#troubleshooting)

## Quick Start

The fastest way to get started:

```bash
cd /Users/rahulraman/Desktop/Tickting-System-Docker/ansible

# 1. Setup environment
chmod +x setup.sh
./setup.sh

# 2. Deploy everything
ansible-playbook deploy-ticketing-system.yml

# 3. Monitor progress
kubectl get pods -n ticketing-system -w
```

Expected deployment time: **30-40 minutes** for first run.

## Prerequisites Verification

Before deploying, verify all prerequisites are met:

### 1. Check Kubernetes Cluster

```bash
# Verify cluster access
kubectl cluster-info

# Expected output:
# Kubernetes control plane is running at https://...
# CoreDNS is running at https://...
```

### 2. Check Node Resources

```bash
# List all nodes
kubectl get nodes

# Check node capacity
kubectl describe nodes

# Check available resources
kubectl top nodes
```

For single-node clusters (Docker Desktop, Minikube), ensure:
- Minimum 4 CPU cores
- Minimum 8 GB RAM
- Minimum 20 GB disk space

### 3. Check Network Access

```bash
# Verify kubeconfig
cat ~/.kube/config | grep cluster:

# Test API access
kubectl api-resources
```

### 4. Install Ansible

```bash
# Check if Ansible is installed
ansible --version

# If not, install:
pip3 install ansible kubernetes

# Verify installation
ansible --version
```

## Initial Setup

### Step 1: Clone/Navigate to Project

```bash
cd /Users/rahulraman/Desktop/Tickting-System-Docker
```

### Step 2: Install Dependencies

```bash
cd ansible
pip3 install -r requirements.txt
```

### Step 3: Install Ansible Collections

```bash
ansible-galaxy collection install -r requirements.yml
```

### Step 4: Verify Setup

```bash
# Check Ansible
ansible --version

# Check Kubernetes
kubectl cluster-info

# Check collections
ansible-galaxy collection list kubernetes.core
```

## Deployment Scenarios

### Scenario 1: Fresh Deployment (Development/Testing)

Use case: Fresh development environment, all components needed

```bash
# 1. Full deployment with all components
ansible-playbook deploy-ticketing-system.yml

# 2. Monitor deployment progress
kubectl get pods -n ticketing-system -w

# 3. Check service status
kubectl get svc -n ticketing-system
```

**Duration**: 30-40 minutes
**Components**: All (Database, Services, Monitoring)

### Scenario 2: Database-First Deployment

Use case: Setting up database infrastructure separately

```bash
# 1. Setup cluster only
ansible-playbook deploy-ticketing-system.yml --tags setup

# 2. Setup database (when ready)
ansible-playbook deploy-ticketing-system.yml --tags database

# 3. Verify database
kubectl get pods -n ticketing-system -l app=postgres
```

**Duration**: 15-20 minutes
**Components**: Database, pgAdmin

### Scenario 3: Microservices Deployment Only

Use case: Database already exists, deploy application services

```bash
# Prerequisites: Database must be running

# 1. Deploy services only
ansible-playbook deploy-ticketing-system.yml --tags services

# 2. Monitor services
kubectl get pods -n ticketing-system -l app!=postgres

# 3. Check service health
kubectl get svc -n ticketing-system
```

**Duration**: 15-25 minutes
**Components**: Services, API Gateway, Frontend

### Scenario 4: Add Monitoring to Existing Setup

Use case: Monitoring already deployed, add more monitoring

```bash
# 1. Deploy monitoring stack
ansible-playbook deploy-ticketing-system.yml --tags monitoring

# 2. Verify monitoring
kubectl get pods -n ticketing-system -l app=prometheus,app=grafana
```

**Duration**: 5-10 minutes
**Components**: Prometheus, Grafana

### Scenario 5: Production Deployment with Custom Settings

Use case: Production environment with specific configurations

```bash
# 1. Create custom variables
cat > group_vars/all.yml << EOF
k8s_namespace: ticketing-system-prod
replica_count: 3
postgres_password: "$(openssl rand -base64 32)"
grafana_admin_password: "$(openssl rand -base64 16)"
EOF

# 2. Deploy to production
ansible-playbook deploy-ticketing-system.yml \
  -e k8s_namespace=ticketing-system-prod \
  -e replica_count=3

# 3. Verify production setup
kubectl get pods -n ticketing-system-prod
```

**Duration**: 40-50 minutes
**Components**: All (Production-grade)

### Scenario 6: Staged Deployment (Multiple Environments)

Use case: Deploy to development, staging, and production

```bash
# Development
ansible-playbook deploy-ticketing-system.yml \
  -e k8s_namespace=ticketing-dev \
  -e replica_count=1

# Staging
ansible-playbook deploy-ticketing-system.yml \
  -e k8s_namespace=ticketing-staging \
  -e replica_count=2

# Production
ansible-playbook deploy-ticketing-system.yml \
  -e k8s_namespace=ticketing-prod \
  -e replica_count=3
```

## Post-Deployment Validation

### Validation Checklist

After deployment, verify all components:

#### 1. Check Namespace

```bash
# List namespace
kubectl get namespace ticketing-system

# Expected: Active
```

#### 2. Verify All Pods

```bash
# List all pods
kubectl get pods -n ticketing-system

# Expected: All pods should be RUNNING (Phase = Running)
```

#### 3. Check Services

```bash
# List services
kubectl get svc -n ticketing-system

# Expected: All services should have CLUSTER-IP
```

#### 4. Verify Database

```bash
# Check PostgreSQL pod
kubectl get pods -n ticketing-system -l app=postgres

# Connect to PostgreSQL
kubectl exec -it -n ticketing-system <postgres-pod> -- psql -U postgres -l
```

#### 5. Test API Gateway

```bash
# Port forward to API Gateway
kubectl port-forward -n ticketing-system svc/api-gateway 8080:8080 &

# Test endpoint
curl http://localhost:8080/eureka/apps

# Kill port forward
kill %1
```

#### 6. Access Monitoring

```bash
# Grafana dashboard
kubectl port-forward -n ticketing-system svc/grafana 3000:3000 &
# Open http://localhost:3000 (admin/admin)

# Prometheus
kubectl port-forward -n ticketing-system svc/prometheus 9090:9090 &
# Open http://localhost:9090

# Kill port forwards
kill %1 %2
```

### Automated Validation Script

```bash
#!/bin/bash
NS="ticketing-system"

echo "Validating Ticketing System Deployment"
echo "========================================"

# Check namespace
echo "✓ Namespace:"
kubectl get namespace $NS

# Check pods
echo "✓ Pods:"
kubectl get pods -n $NS

# Check services
echo "✓ Services:"
kubectl get svc -n $NS

# Check replicas
echo "✓ Deployment Replicas:"
kubectl get deployment -n $NS -o wide

# Check pod logs for errors
echo "✓ Recent Errors:"
kubectl logs -n $NS --tail=10 --all-containers=true -l app --timestamps=true | grep -i error || echo "No errors found"
```

## Troubleshooting

### Issue: Pods Not Starting

**Symptoms**: Pods stuck in Pending, CrashLoopBackOff, or ImagePullBackOff state

**Solution**:

```bash
# 1. Describe the pod
kubectl describe pod -n ticketing-system <pod-name>

# 2. Check logs
kubectl logs -n ticketing-system <pod-name>

# 3. Check events
kubectl get events -n ticketing-system --sort-by='.lastTimestamp'

# 4. Common fixes:
# - Check image availability: kubectl get images
# - Check resource limits: kubectl top nodes
# - Check storage: kubectl get pvc -n ticketing-system
```

### Issue: Database Connection Failed

**Symptoms**: Services can't connect to PostgreSQL

**Solution**:

```bash
# 1. Verify PostgreSQL is running
kubectl get pods -n ticketing-system -l app=postgres

# 2. Check PostgreSQL logs
kubectl logs -n ticketing-system <postgres-pod>

# 3. Test PostgreSQL connectivity
kubectl run -n ticketing-system postgres-test --image=postgres:15 -it \
  -- psql -h postgres -U postgres -c "SELECT 1;"

# 4. Verify services
kubectl get svc -n ticketing-system -l app=postgres
```

### Issue: Services Can't Reach Each Other

**Symptoms**: Service-to-service communication failing

**Solution**:

```bash
# 1. Check DNS resolution
kubectl exec -it -n ticketing-system <pod> -- nslookup <service-name>

# 2. Test connectivity
kubectl exec -it -n ticketing-system <pod> -- curl http://<service-name>:8080

# 3. Check network policies
kubectl get networkpolicy -n ticketing-system

# 4. Verify service endpoints
kubectl get endpoints -n ticketing-system
```

### Issue: Insufficient Resources

**Symptoms**: Pods pending due to resource constraints

**Solution**:

```bash
# 1. Check node resources
kubectl top nodes
kubectl describe node <node-name>

# 2. Scale down if needed
kubectl scale deployment -n ticketing-system <service> --replicas=1

# 3. Check pod resource requests
kubectl get pods -n ticketing-system -o yaml | grep -A5 resources:

# 4. For Docker Desktop:
# - Increase resources: Docker Desktop > Settings > Resources
# - Minimum recommended: 4 CPUs, 8GB RAM
```

### Issue: Deployment Takes Too Long

**Symptoms**: Deployment hanging at certain stage

**Solution**:

```bash
# 1. Check what's happening
kubectl get pods -n ticketing-system -w

# 2. Check recent activity
kubectl get events -n ticketing-system --sort-by='.lastTimestamp' | tail -20

# 3. Check pod logs
kubectl logs -n ticketing-system <pod-name> --tail=50

# 4. Increase timeout if needed:
# Edit deploy-ticketing-system.yml and increase 'retries' value
```

### Cleanup and Retry

If deployment fails, clean up and retry:

```bash
# 1. Remove failed deployment
ansible-playbook deploy-ticketing-system.yml -e "perform_cleanup=true"

# 2. Verify cleanup
kubectl get pods -n ticketing-system

# 3. Retry deployment
ansible-playbook deploy-ticketing-system.yml

# 4. For complete reset (delete namespace):
ansible-playbook deploy-ticketing-system.yml \
  -e "perform_cleanup=true" \
  -e "cleanup_namespace=true"

# 5. Check namespace deleted
kubectl get namespace ticketing-system  # Should not exist
```

## Advanced Operations

### Scaling Services

```bash
# Scale specific service to 3 replicas
kubectl scale deployment -n ticketing-system member-service --replicas=3

# Verify scaling
kubectl get deployment -n ticketing-system member-service
```

### Rolling Updates

```bash
# Update image for a service
kubectl set image deployment -n ticketing-system member-service \
  member-service=myregistry.azurecr.io/member-service:v2.0

# Monitor rollout
kubectl rollout status deployment -n ticketing-system member-service
```

### Backup and Restore

```bash
# Backup database
kubectl exec -n ticketing-system <postgres-pod> -- \
  pg_dump -U postgres expeditionDB > backup_$(date +%Y%m%d).sql

# Restore database
kubectl exec -i -n ticketing-system <postgres-pod> -- \
  psql -U postgres expeditionDB < backup.sql
```

## Performance Tuning

### Monitor Resource Usage

```bash
# CPU and Memory usage
kubectl top pods -n ticketing-system

# Detailed metrics
kubectl get pods -n ticketing-system -o json | \
  jq '.items[] | {name: .metadata.name, cpu: .spec.containers[].resources.requests.cpu, memory: .spec.containers[].resources.requests.memory}'
```

### Optimize Resource Requests

Edit the YAML files in `k8s/` directory and adjust:
- `resources.requests.cpu`
- `resources.requests.memory`
- `resources.limits.cpu`
- `resources.limits.memory`

## Support

For issues or questions:
1. Check [Troubleshooting](#troubleshooting) section
2. Review logs: `kubectl logs -n ticketing-system <pod>`
3. Check events: `kubectl get events -n ticketing-system`
4. Consult: [Kubernetes Docs](https://kubernetes.io/docs/)

## Next Steps

After successful deployment:
1. Configure application settings
2. Set up monitoring dashboards
3. Configure backups
4. Set up CI/CD pipeline
5. Plan for scaling and HA
