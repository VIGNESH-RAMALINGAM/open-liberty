-include= ~${workspace}/cnf/resources/bnd/feature.props
symbolicName=io.openliberty.jakarta.authentication-3.1
singleton=true
-features=\
  com.ibm.websphere.appserver.eeCompatible-11.0, \
  io.openliberty.noShip-1.0
-bundles=\
  io.openliberty.jakarta.authentication.3.1; location:=dev/api/spec/; mavenCoordinates="jakarta.authentication:jakarta.authentication-api:3.1.0-M1"
kind=noship
edition=full

