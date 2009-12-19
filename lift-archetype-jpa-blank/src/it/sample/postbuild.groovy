result = new File(basedir, 'sample-project/spa/target/sample-project-spa-0.1.jar').exists()
result &= new File(basedir, 'sample-project/web/target/sample-project-web-0.1.war').exists()

return result
