//var/Slack.groovy

import groovy.json.JsonOutput
import java.util.Optional
import hudson.model.Actionable
import hudson.tasks.junit.CaseResult


def notifySlack(text, channel, attachments) {
    def slackURL = 'https://hooks.slack.com/services/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'
    def jenkinsIcon = 'https://wiki.jenkins-ci.org/download/attachments/2916393/logo.png'

    def payload = JsonOutput.toJson([text: text,
        channel: channel,
        username: "Jenkins",
        icon_url: jenkinsIcon,
        attachments: attachments
    ])

    sh "curl -X POST --data-urlencode \'payload=${payload}\' ${slackURL}"
}

def getGitAuthor() {
    def commit = sh(returnStdout: true, script: 'git rev-parse HEAD')
    author = sh(returnStdout: true, script: "git --no-pager show -s --format='%an' ${commit}").trim()
}

def getLastCommitMessage() {
    message = sh(returnStdout: true, script: 'git log -1 --pretty=%B').trim()
}

def call() {

    def slackNotificationChannel = "spam"

    //Get commit detail
    getGitAuthor()
    getLastCommitMessage()

    //Send notification to slack
    notifySlack("", slackNotificationChannel, [
    [
        title: "${env.JOB_NAME}, build #${env.BUILD_NUMBER}",
        title_link: "${env.BUILD_URL}",
        color: "danger",
        author_name: "${author}",
        text: "${currentBuild.currentResult}",
        "mrkdwn_in": ["fields"],
        fields: [
            [
                title: "Branch:",
                value: "${env.GIT_BRANCH}",
                short: true
            ],
            [
                title: "Last Commit:",
                value: "${message}",
                short: false
            ]
                ]
    ]
    
    ])

}