pipeline {
    environment {
        SPLITERASH_REPO = credentials('gradle_spliterash_repo')
    }

    agent {
        docker {
            image 'adoptopenjdk:11'
            reuseNode true
        }
    }

    stages {
        stage('build') {
            steps {
                sh 'sh gradlew publish'
            }
        }
    }
}