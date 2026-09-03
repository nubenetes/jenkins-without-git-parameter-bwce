#!/usr/bin/env bash
set -euo pipefail

echo "===> [Credentials] Generating secure platform tokens & secrets..."
kubectl create secret generic jenkins-api-tokens \
  --from-literal=release-dispatch-token="RELEASE_DISPATCH_TOKEN_2026" \
  --namespace=jenkins \
  --dry-run=client -o yaml | kubectl apply -f -

# ArgoCD GitHub Token for ApplicationSet PR Preview Generator
kubectl create namespace argocd --dry-run=client -o yaml | kubectl apply -f -
kubectl create secret generic github-token-secret \
  --from-literal=token="ghp_mock_token_for_bwce_preview_2026" \
  --namespace=argocd \
  --dry-run=client -o yaml | kubectl apply -f -
