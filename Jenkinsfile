node {
   stage('Clone') {
      checkout scm
   }
   
   stage('Build') {
      def revision = env.BRANCH_NAME == 'master' ? '1.0' : env.BRANCH_NAME
      sh "${tool 'M3'}/bin/mvn clean deploy -Drevision=${revision} -U"
   }
   
   stage('Archive') {
      archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
   }
}
