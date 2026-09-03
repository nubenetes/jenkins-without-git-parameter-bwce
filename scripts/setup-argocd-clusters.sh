#!/usr/bin/env bash
set -euo pipefail

echo "===> [ArgoCD 3.5] Configuring Multi-Cluster Connections for BWCE..."

# Cluster 1: OCP DEV (In-Cluster)
cat << 'SECRET' | kubectl apply -f -
apiVersion: v1
kind: Secret
metadata:
  name: cluster-ocp-dev
  namespace: argocd
  labels:
    argocd.argoproj.io/secret-type: cluster
    environment: dev
type: Opaque
stringData:
  name: in-cluster
  server: https://kubernetes.default.svc
  config: |
    {
      "tlsClientConfig": {
        "insecure": false
      }
    }
SECRET

# Cluster 2: OCP STAGING
cat << 'SECRET' | kubectl apply -f -
apiVersion: v1
kind: Secret
metadata:
  name: cluster-ocp-staging
  namespace: argocd
  labels:
    argocd.argoproj.io/secret-type: cluster
    environment: staging
type: Opaque
stringData:
  name: ocp-staging-cluster
  server: https://api.ocp-staging.nubenetes.internal:6443
  config: |
    {
      "tlsClientConfig": {
        "insecure": true
      }
    }
SECRET

# Cluster 3: OCP PROD
cat << 'SECRET' | kubectl apply -f -
apiVersion: v1
kind: Secret
metadata:
  name: cluster-ocp-prod
  namespace: argocd
  labels:
    argocd.argoproj.io/secret-type: cluster
    environment: prod
type: Opaque
stringData:
  name: ocp-prod-cluster
  server: https://api.ocp-prod.nubenetes.internal:6443
  config: |
    {
      "tlsClientConfig": {
        "insecure": true
      }
    }
SECRET

echo "ArgoCD multi-cluster topology ready."
