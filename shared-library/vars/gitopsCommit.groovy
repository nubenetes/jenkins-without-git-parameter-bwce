// ==============================================================================
// Shared Library Step: gitopsCommit.groovy (TIBCO BWCE GitOps Platform)
// Updates environment manifests in GitOps repository and commits changes
// ==============================================================================

def call(Map config = [:]) {
    def envName    = config.envName ?: 'dev'
    def appName    = config.appName ?: 'tibco-bwce-order-service'
    def imageTag   = config.imageTag ?: error("gitopsCommit requires 'imageTag'")
    def gitopsRepo = config.gitopsRepo ?: env.GITOPS_REPO ?: 'https://github.com/nubenetes/jenkins-without-git-parameter-bwce.git'
    def commitMsg  = config.commitMsg ?: "chore(gitops): update ${appName} to ${imageTag} for ${envName} [skip ci]"

    echo "📝 [TIBCO BWCE GitOps Update] Updating image tag for '${appName}' in environment '${envName}' to '${imageTag}'..."

    dir('gitops-workspace') {
        sh """
            git config --global user.name "Nubenetes BWCE GitOps Bot"
            git config --global user.email "gitops-bwce@nubenetes.io"

            # Update Kustomize overlay if present
            KUSTOMIZE_OVERLAY="sample-apps/${appName}/k8s/overlays/${envName}/kustomization.yaml"
            if [ -f "\$KUSTOMIZE_OVERLAY" ]; then
                echo "Updating Kustomize overlay \$KUSTOMIZE_OVERLAY..."
                sed -i "s/newTag:.*/newTag: \\"${imageTag}\\"/g" "\$KUSTOMIZE_OVERLAY" || true
            fi

            # Check if changes exist
            if git status --porcelain | grep -E "(sample-apps)"; then
                echo "✅ Changes detected. Committing to GitOps repository..."
                git add sample-apps/
                git commit -m "${commitMsg}"
                echo "🚀 GitOps commit created: ${commitMsg}"
            else
                echo "ℹ️ No GitOps manifest changes detected (already at tag ${imageTag})."
            fi
        """
    }
}
