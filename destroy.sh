#!/usr/bin/env bash
# ==============================================================================
# Clean Decommission Script (TIBCO BWCE GitOps Platform)
# ==============================================================================
set -euo pipefail

echo "======================================================================"
echo "🧹 DECOMMISSIONING TIBCO BWCE PLATFORM: Jenkins Without Git Parameter"
echo "======================================================================"

kubectl delete -f argocd-apps/ --ignore-not-found || true

if command -v helm &>/dev/null; then
    helm uninstall jenkins -n jenkins || true
    helm uninstall argocd -n argocd || true
    helm uninstall datadog -n datadog || true
fi

kubectl delete namespace jenkins argocd datadog nubenetes-dev-bwce nubenetes-staging-bwce nubenetes-prod-bwce --ignore-not-found || true

echo "✅ All TIBCO BWCE namespaces and resources cleanly terminated."
