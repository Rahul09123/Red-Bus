#!/bin/bash

set -e

echo "======================================"
echo "Ticketing System Ansible Setup Script"
echo "======================================"
echo ""

# Check Python
echo "[1/5] Checking Python installation..."
if ! command -v python3 &> /dev/null; then
    echo "❌ Python 3 not found. Please install Python 3.7+"
    exit 1
fi
PYTHON_VERSION=$(python3 --version 2>&1 | awk '{print $2}')
echo "✓ Python $PYTHON_VERSION found"
echo ""

# Check pip
echo "[2/5] Checking pip installation..."
if ! command -v pip3 &> /dev/null; then
    echo "❌ pip3 not found. Please install pip3"
    exit 1
fi
echo "✓ pip3 found"
echo ""

# Install Python dependencies
echo "[3/5] Installing Python dependencies..."
pip3 install -r requirements.txt
echo "✓ Python dependencies installed"
echo ""

# Check Ansible
echo "[4/5] Checking Ansible installation..."
if ! command -v ansible &> /dev/null; then
    echo "❌ Ansible not found after installation"
    exit 1
fi
ANSIBLE_VERSION=$(ansible --version | head -n 1)
echo "✓ $ANSIBLE_VERSION"
echo ""

# Install Galaxy collections
echo "[5/5] Installing Ansible Galaxy collections..."
if [ -f "requirements.yml" ]; then
    ansible-galaxy collection install -r requirements.yml
    echo "✓ Galaxy collections installed"
else
    echo "⚠ requirements.yml not found, skipping Galaxy collection installation"
fi
echo ""

# Check Kubernetes
echo "[BONUS] Checking Kubernetes access..."
if command -v kubectl &> /dev/null; then
    echo "✓ kubectl found: $(kubectl version --client --short 2>/dev/null || echo 'version check failed')"
    
    if kubectl cluster-info &> /dev/null; then
        echo "✓ Connected to Kubernetes cluster"
        echo "  Cluster info:"
        kubectl cluster-info | sed 's/^/  /'
    else
        echo "⚠ kubectl installed but not connected to a cluster"
        echo "  Configure your kubeconfig before deploying"
    fi
else
    echo "⚠ kubectl not found. Install it from: https://kubernetes.io/docs/tasks/tools/"
fi
echo ""

echo "======================================"
echo "✓ Setup complete!"
echo "======================================"
echo ""
echo "Next steps:"
echo "1. Configure your kubeconfig file (~/.kube/config)"
echo "2. Verify Kubernetes access: kubectl cluster-info"
echo "3. Review ansible/README.md for deployment options"
echo "4. Run: ansible-playbook deploy-ticketing-system.yml"
echo ""
echo "For more information:"
echo "- Documentation: ansible/README.md"
echo "- Ansible docs: https://docs.ansible.com/"
echo "- Kubernetes docs: https://kubernetes.io/docs/"
echo ""
