pipeline {
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