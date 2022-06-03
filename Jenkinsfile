pipeline {
    environment {
        SPLITERASH_REPO = credentials('gradle_spliterash_repo')
    }

    agent {
        docker {
            image 'adoptopenjdk:11'
            reuseNode true
            args '-v $HOME/.gradle:/root/.gradle -w /root/.gradle'
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