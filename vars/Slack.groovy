//var/Slack.groovy

import groovy.json.JsonOutput
import java.util.Optional
import hudson.model.Actionable
import hudson.tasks.junit.CaseResult

def message = ""
def author = ""

def getGitAuthor = {
    def commit = sh(returnStdout: true, script: 'git rev-parse HEAD')
    author = sh(returnStdout: true, script: "git --no-pager show -s --format='%an' ${commit}").trim()
}

def getLastCommitMessage = {
    message = sh(returnStdout: true, script: 'git log -1 --pretty=%B').trim()
}

def populateGlobalVariables = {
    getLastCommitMessage()
    getGitAuthor()
}