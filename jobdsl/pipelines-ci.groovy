// ==============================================================================
// Job DSL: Pure CI Multibranch Pipelines for TIBCO BWCE (Zero Git-Parameter Plugin)
// ==============================================================================

def apps = [
    [
        name: 'tibco-bwce-order-service',
        description: 'TIBCO BWCE Order Service Cloud-Native Microservice',
        repoUrl: 'https://github.com/nubenetes/jenkins-without-git-parameter-bwce.git',
        jenkinsfile: 'jenkinsfiles/ci/Jenkinsfile.app-bwce'
    ],
    [
        name: 'tibco-bwce-customer-api',
        description: 'TIBCO BWCE Customer API Cloud-Native Microservice',
        repoUrl: 'https://github.com/nubenetes/jenkins-without-git-parameter-bwce.git',
        jenkinsfile: 'jenkinsfiles/ci/Jenkinsfile.app-bwce'
    ]
]

// Multibranch Pipeline: Automatically discovers Branches, Tags, and PRs from Git
apps.each { app ->
    multibranchPipelineJob("01-CI-Build-Pipelines/${app.name}") {
        description("""
        🚀 <b>Pure GitOps CI Multibranch Pipeline: ${app.name}</b><br/>
        ${app.description}<br/>
        • <b>Source Repository</b>: ${app.repoUrl}<br/>
        • <b>Trigger Model</b>: Webhook & Git Push / PR (Zero UI parameters required)<br/>
        • <b>GitOps Integration</b>: Builds EAR, packages container, signs with Cosign, and auto-commits tag to ArgoCD GitOps repo.
        """.stripIndent())

        branchSources {
            git {
                id("${app.name}-git-source")
                remote(app.repoUrl)
                includes('main develop staging release/* PR-*')
            }
        }

        orphanedItemStrategy {
            discardOldItems {
                numToKeep(20)
                daysToKeep(15)
            }
        }

        factory {
            workflowBranchProjectFactory {
                scriptPath(app.jenkinsfile)
            }
        }

        triggers {
            periodicFolderTrigger {
                interval('5m')
            }
        }
    }
}
