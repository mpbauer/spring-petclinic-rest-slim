[![Spring Boot](docs/images/logo-spring.png)](https://spring.io/)

# [Spring PetClinic Sample Application (Slim)](https://github.com/mpbauer/spring-petclinic-rest-slim)

This backend version of the Spring Petclinic application only provides a REST API. **There is no UI**.
The [spring-petclinic-angular project](https://github.com/spring-petclinic/spring-petclinic-angular) is a Angular
front-end application which consumes the REST API.

A modified version (specific to this project) of
the [spring-petclinic-angular project](https://github.com/spring-petclinic/spring-petclinic-angular) can be
found [HERE](https://github.com/mpbauer/spring-petclinic-angular)

---
**NOTE**

This project is based upon code from the commit reference `f97f1d44580d9950463043b451902f2e4955dea7` of the repository [spring-petclinic/spring-petclinic-rest](https://github.com/spring-petclinic/spring-petclinic-rest).

---

## Understanding the Spring Petclinic application with a few diagrams

[See the presentation of the Spring Petclinic Framework version](http://fr.slideshare.net/AntoineRey/spring-framework-petclinic-sample-application)

### Petclinic ER Model

![alt petclinic-ermodel](petclinic-ermodel.png)

## Running the petclinic application locally

### With Maven

```
./mvnw spring-boot:run
```

## Packaging and running the application with JVM

The application ca be packaged using:

```shell script
./mnvw package
```

It produces the spring-petclinic-rest-slim-{{version}}.jar file in the /target directory. The jar is already a fat-jar
and can be executed with the following command:

```shell script
java -jar ./target/spring-petclinic-rest-slim-{{version}}.jar
```

### With Docker

Before building the container image run:

```shell script
./mvnw package
```

Then, build the image with:

```shell script
docker build -f src/main/docker/Dockerfile.jvm -t mpbauer/spring-petclinic-rest-slim-jvm .
```

Then run the container using:

```shell script
docker run -i --rm -p 8080:8080 mpbauer/spring-petclinic-rest-slim-jvm
```

You can then access petclinic here: [http://localhost:8080/petclinic/](http://localhost:8080/petclinic/)

## Creating a native executable

[Spring Native](https://github.com/spring-projects-experimental/spring-native) provides support to compile Spring
application to a native executable by utilizing [GraalVM](https://www.graalvm.org/).

:construction: Warning: This section is under heavy construction

> At the time of writing the [spring-native](https://github.com/spring-projects-experimental/spring-native)
> project is still in alpha and the number of supported dependencies is still relatively small.
> A native configuration will be implemented once the `spring-native` project is more stable and supports a wider range of dependencies.

## Open API REST Documentation

The following URLs can be used to access a documentation about
the [spring-petclinic-rest-slim](https://github.com/mpbauer/spring-petclinic-rest-slim) application:

**Swagger UI**

```
http://localhost:8080/petclinic/swagger-ui
```

**Open API Schema Document**

```
http://localhost:8080/petclinic/v3/api-docs
```

## Health Checks

The `spring-boot-starter-actuator` dependency provides a health check endpoint at `/actuator/health`

Example:

```
http://localhost:8080/petclinic/actuator/health
```

## Metrics

The `spring-boot-starter-actuator` provides two metric endpoints:

- `/actuator/metrics` - Shows ‘metrics’ information for the current application.
- `/actuator/prometheus` - Exposes metrics in a format that can be scraped by a Prometheus server. Requires a dependency
  on `micrometer-registry-prometheus`

**Metrics endpoint:**

```
http://localhost:8080/petclinic/actuator/metrics
```

**Prometheus endpoint:**

```
http://localhost:8080/petclinic/actuator/prometheus
```

## Database configuration

The database support for this version of
the [spring-petlinic-rest](https://github.com/spring-petclinic/spring-petclinic-rest) project was significantly reduced.
As of now this project only supports [PostgreSQL](https://www.postgresql.org/)
and [H2](https://www.h2database.com/html/main.html).

In its default configuration a `PostgreSQL` database is required to run the application. For the execution of tests an
embedded `H2` is started.

For local development you may want to start a `PostgreSQL` database with `Docker`:

````
docker run --name petclinic -p 5432:5432 -e POSTGRES_PASSWORD=pass -d postgres
````

## Security configuration

A Role Based Access Control is enabled by default when running the application with the `default` profile. When you
start the application with the `local` profile through `./mvnw spring-boot:run -Dspring-boot.run.profiles=local`
authentication is disabled by default.

> At the moment JWT based authentication is not implemented yet

### Enable Authentication

In order to use the JWT based authentication functionality, you can turn it on by setting the following property in
the `application.properties` file:

```properties
petclinic.security.enable=true
```

### Authorization

This will secure all APIs and in order to access them, basic authentication is required. Apart from authentication, APIs
also require authorization. This is done via roles that a user can have. The existing roles are listed below with the
corresponding permissions:

Role         | Controller
----------   | ----------------
OWNER_ADMIN  | OwnerController<br/>PetController<br/>PetTypeController (`getAllPetTypes()` & `getPetType()`)
VET_ADMIN    | PetTypeController<br/>SpecialityController</br>VetController
ADMIN        | UserController

### Roles Based Access Control with predefined JSON Web Tokens

To make the use of this sample application as easy as possible a set of fixed JSON Web Tokens with different roles were
generated and signed with a secret that is hardcoded in `application.properties`. You can copy the pre-created tokens
and pass it via the `Authorization` header to the application.

**Example:**

```shell
curl --location --request GET 'http://localhost:8080/petclinic/api/owners' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJYMDAwMDQiLCJpc3MiOiJodHRwczovL3NwcmluZy1wZXRjbGluaWMuZ2l0aHViLmlvL2lzc3VlciIsImdyb3VwcyI6WyJST0xFX09XTkVSX0FETUlOIiwiUk9MRV9WRVRfQURNSU4iLCJST0xFX0FETUlOIl0sImV4cCI6NDc3MDU1NDgzNCwiaWF0IjoxNjE0ODc3NjQxLCJqdGkiOiIwYTU2ZmU5My05YzU1LTRhZWYtYjRjZS1mMTg3YTcwMWYwYTYifQ.MEQQrmnNfoNTxVT1ZOiFT-UDGqzbv12u6jAlwPvKjSaaJYV84QKU3NTU79mp9hdHY3J7sKgfTGGJ1qMJtfSFpg'
```

> :exclamation: IMPORTANT: Never push a secret hardcoded and in plaintext in a Git repository. This application is just
> for showcasing the use of JSON Web Tokens in the Petclinic application but it should never be done this way for real applications.

#### List of valid JSON Web Tokens:

Role: `OWNER_ADMIN`

```
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJYMDAwMDEiLCJncm91cHMiOlsiUk9MRV9PV05FUl9BRE1JTiJdLCJleHAiOjQ3NzA1NTUyMTEsImlhdCI6MTYxNDg3ODAxOCwianRpIjoiMzU0NzZkNWEtMjA1OC00YzliLTlhMDMtMzkxZmJkOTgyMWU3In0.qwMEcq7l3m4ppazuPpVKGhm4q1KOeNQeK_wC09iUjvs1wngFszU8fwhBVa4DuwHuVTxgo2WGGLZCr5XsRc3kgA
```

Role: `VET_ADMIN`

```
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJYMDAwMDIiLCJncm91cHMiOlsiUk9MRV9WRVRfQURNSU4iXSwiZXhwIjo0NzcwNTU1MjExLCJpYXQiOjE2MTQ4NzgwMTgsImp0aSI6ImU5ODNhMzY0LTNhMjMtNDRlMi05YTE0LTE4ZTNmYWQ2ZGRiZCJ9.8EWFF19GhHtjvIxQENnYkgLeCHje5RY2Jn67JyeErQDLsK6lZxspzmF7Eb2v-D1b_abf7Hxx0fWhtjVLQMEPPA
```

Role: `ADMIN`

```
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJYMDAwMDMiLCJncm91cHMiOlsiUk9MRV9BRE1JTiJdLCJleHAiOjQ3NzA1NTUyMTEsImlhdCI6MTYxNDg3ODAxOCwianRpIjoiOTU1YWVlOGEtYzFkYi00YmJkLTg0ZGMtYjQwMmZkOWI2OTc3In0.4iXV2RBTwT35CkzPwOz2w1q54ts0_ozsyOE980tMpWcTJVBQw7bJBl2Y7Z_Dc-heUbI4VvBlTLGEhyBZIN8B-g
```

Roles: `OWNER_ADMIN`, `VET_ADMIN`, `ADMIN`

```
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJYMDAwMDQiLCJncm91cHMiOlsiUk9MRV9PV05FUl9BRE1JTiIsIlJPTEVfVkVUX0FETUlOIiwiUk9MRV9BRE1JTiJdLCJleHAiOjQ3NzA1NTUyMTEsImlhdCI6MTYxNDg3ODAxOCwianRpIjoiNGVmMjY0MjctYWUyNi00MGJjLWEzYjYtYzk3NGE0YTc0YWEzIn0.ypi1TQiGEuWqTKZwNe1pqMn7aIGXPCgMfONEoYmL2mrYW66vF5B3wFxVHyApcOOqttqNWtxNw7kPCdmqtW730Q
```

## GitHub Actions CI/CD configuration

This section explains the necessary setup details to build and deploy the `quarkus-petclinic-rest` application
to [Google Cloud Platform (GCP)](https://cloud.google.com/)
and [Google Kubernetes Engine](https://cloud.google.com/kubernetes-engine) where we use [Knative](https://knative.dev/)
for serverless deployments.

### Service Accounts

GitHub Actions is building the container images with [Google Cloud Build](https://cloud.google.com/build) and stores the
resulting container image in [Google Container Registry](https://cloud.google.com/container-registry). Afterwards the
image is going to be deployed to [Google Cloud Run](https://cloud.google.com/run)
and [Google Kubernetes Engine (GKE)](https://cloud.google.com/kubernetes-engine). To do this with a CI/CD tool like
[GitHub Actions](https://docs.github.com/en/actions) we need to create Service Accounts to manage and regulate access to
Google Cloud ressources.

### Prerequisites

Before you start you should have already set up a [GCP account](https://cloud.google.com/gcp) with
a [billing account](https://cloud.google.com/billing/docs/how-to/manage-billing-account) as well as a project. There are
several ways to set up a service account with GCP. You can either use the Google Cloud SDK or the Management Console in
your browser to create and configure service accounts.

### Create a Service Account with `gcloud`

1) Export these environment variables so that you can copy and paste the following commands:

    ```shell
    export PROJECT_ID=<YOUR PROJECT ID>
    export SERVICE_ACCOUNT_NAME=<ENTER A NAME FOR YOUR SERVICE ACCOUNT>
    ```

2) Log in with your Google account:

    ```shell
    gcloud auth login
    ```

3) Select the project configured via `$PROJECT_ID`:

    ```shell
    gcloud config set project $PROJECT_ID
    ```

4) Enable the necessary services:

    ```shell
    gcloud services enable cloudbuild.googleapis.com run.googleapis.com containerregistry.googleapis.com container.googleapis.com
    ```

5) Create a service account:

    ```shell
    gcloud iam service-accounts create $SERVICE_ACCOUNT_NAME \
      --description="GitHub Actions service account for Petclinic repositories" \
      --display-name="$SERVICE_ACCOUNT_NAME"
    ```

6) Give the service account Cloud Run Admin, Storage Admin, and Service Account User roles. You can’t set all of them at
   once, so you have to run separate commands:

    ```shell
    gcloud projects add-iam-policy-binding $PROJECT_ID \
      --member=serviceAccount:$SERVICE_ACCOUNT_NAME@$PROJECT_ID.iam.gserviceaccount.com \
      --role=roles/cloudbuild.builds.editor
    
    gcloud projects add-iam-policy-binding $PROJECT_ID \
      --member=serviceAccount:$SERVICE_ACCOUNT_NAME@$PROJECT_ID.iam.gserviceaccount.com \
      --role=roles/iam.serviceAccountUser
    
    gcloud projects add-iam-policy-binding $PROJECT_ID \
      --member=serviceAccount:$SERVICE_ACCOUNT_NAME@$PROJECT_ID.iam.gserviceaccount.com \
      --role=roles/run.admin
     
    gcloud projects add-iam-policy-binding $PROJECT_ID \
      --member=serviceAccount:$SERVICE_ACCOUNT_NAME@$PROJECT_ID.iam.gserviceaccount.com \
      --role=roles/viewer
      
    gcloud projects add-iam-policy-binding $PROJECT_ID \
      --member=serviceAccount:$SERVICE_ACCOUNT_NAME@$PROJECT_ID.iam.gserviceaccount.com \
      --role=roles/storage.admin
      
    gcloud projects add-iam-policy-binding $PROJECT_ID \
      --member=serviceAccount:$SERVICE_ACCOUNT_NAME@$PROJECT_ID.iam.gserviceaccount.com \
      --role=roles/container.admin  
    ```

7) Generate a key.json file with your credentials, so your GitHub workflow can authenticate with Google Cloud. After
   issuing the following command you can find the generated key in your current folder.

    ```shell
    gcloud iam service-accounts keys create key.json \
        --iam-account $SERVICE_ACCOUNT_NAME@$PROJECT_ID.iam.gserviceaccount.com
    ```

   The following tutorial explains this in more
   detail: [link](https://cloud.google.com/community/tutorials/cicd-cloud-run-github-actions)

### Create a Service Account with [GCP Management Console](https://console.cloud.google.com/)

1) Open the [Google Cloud Management Console](https://console.cloud.google.com) in your browser

2) Select your project with the dropdown on the top navigation bar

   [![SETUP_GCP_SERVICE_ACCOUNT_00](docs/screenshots/setup-gcp-service-account/setup_gcp_service_account_00.png)](docs/screenshots/setup-gcp-service-account/setup_gcp_service_account_00.png)

3) Go
   to [IAM & Admin](https://console.cloud.google.com/iam-admin/) > [Service Accounts](https://console.cloud.google.com/iam-admin/serviceaccounts)
   and click `ADD`to create a new service account

4) Enter a name and a description (optional) for your new service account and click `CREATE`

   [![SETUP_GCP_SERVICE_ACCOUNT_01](docs/screenshots/setup-gcp-service-account/setup_gcp_service_account_01.png)](docs/screenshots/setup-gcp-service-account/setup_gcp_service_account_01.png)

5) Add the following roles to your service account and click `DONE`. The third step is not necessary and can be skipped.

    - `Cloud Build Editor (roles/cloudbuild.builds.editor)` - Required for Cloud Build
    - `Service Account User (roles/iam.serviceAccountUser)` - General Service Account permissions
    - `Cloud Run Admin (roles/run.admin)` - Required for Cloud Run
    - `Viewer (roles/viewer)` - Required as a workaround for successful deployments in GitHub Actions (see explanation
      below)
    - `Storage Admin (roles/storage.admin)` - Required for Container Registry
    - `Kubernetes Engine Admin (roles/container.admin)` - Required for GKE deployments

   > Explanation of the Viewer Role:
   >
   > Link: https://towardsdatascience.com/deploy-to-google-cloud-run-using-github-actions-590ecf957af0
   > Once the service account is created you will need to select the following roles. I tried a number of different ways to remove the very permissive project viewer role, but at the time of this writing this your service account will need this role or the deployment will appear to fail in Github even if it is successfully deployed to Cloud Run.

   [![SETUP_GCP_SERVICE_ACCOUNT_02](docs/screenshots/setup-gcp-service-account/setup_gcp_service_account_02.png)](docs/screenshots/setup-gcp-service-account/setup_gcp_service_account_02.png)

6) Now click on your newly created service account and click `ADD KEY` on the service account details page. This will
   create new credentials which will be later used to authenticate your service account. Select `JSON`and click `CREATE`
   to generate and download your service account credentials.

   [![SETUP_GCP_SERVICE_ACCOUNT_03](docs/screenshots/setup-gcp-service-account/setup_gcp_service_account_03.png)](docs/screenshots/setup-gcp-service-account/setup_gcp_service_account_03.png)

7) Your service account credentials have been generated and downloaded on your machine. Make sure to keep them safe!

   [![SETUP_GCP_SERVICE_ACCOUNT_04](docs/screenshots/setup-gcp-service-account/setup_gcp_service_account_04.png)](docs/screenshots/setup-gcp-service-account/setup_gcp_service_account_04.png)

### Setup Google Kubernetes Engine

1) Export these environment variables so that you can copy and paste the following commands:

    ```shell
    export PROJECT_ID=<YOUR PROJECT ID>
    export SERVICE_ACCOUNT_NAME=<ENTER A NAME FOR YOUR SERVICE ACCOUNT>
    ```

2) Log in with your Google account:

    ```shell
    gcloud auth login
    ```

3) Select the project configured via `$PROJECT_ID`:

    ```shell
    gcloud config set project $PROJECT_ID
    ```

4) Enable the necessary services (if not already enabled):

    ```shell
    gcloud services enable container.googleapis.com
    ```

5) Create GKE Cluster

    ```shell
    gcloud beta container --project "$PROJECT_ID" clusters create "petclinic-cluster" \
    --zone "europe-west3-c" \
    --no-enable-basic-auth \
    --cluster-version "1.18.12-gke.1210" \
    --release-channel "regular" \
    --machine-type "n2-standard-4" \
    --image-type "COS" \
    --disk-type "pd-standard" \
    --disk-size "100" \
    --metadata disable-legacy-endpoints=true \
    --scopes "https://www.googleapis.com/auth/devstorage.read_only","https://www.googleapis.com/auth/logging.write","https://www.googleapis.com/auth/monitoring","https://www.googleapis.com/auth/servicecontrol","https://www.googleapis.com/auth/service.management.readonly","https://www.googleapis.com/auth/trace.append" \
    --num-nodes "1" \
    --enable-stackdriver-kubernetes \
    --enable-ip-alias \
    --network "projects/$PROJECT_ID/global/networks/default" \
    --subnetwork "projects/$PROJECT_ID/regions/europe-west3/subnetworks/default" \
    --default-max-pods-per-node "110" \
    --no-enable-master-authorized-networks \
    --addons HorizontalPodAutoscaling,HttpLoadBalancing,GcePersistentDiskCsiDriver \
    --enable-autoupgrade \
    --enable-autorepair \
    --max-surge-upgrade 1 \
    --max-unavailable-upgrade 0 \
    --enable-shielded-nodes \
    --node-locations "europe-west3-c"
    ```

   After the cluster was successfuly created you should see the following output:

    ```
    NAME               LOCATION        MASTER_VERSION    MASTER_IP      MACHINE_TYPE   NODE_VERSION      NUM_NODES  STATUS
    petclinic-cluster  europe-west3-c  1.18.12-gke.1210  X.X.X.X        n2-standard-4  1.18.12-gke.1210  1          RUNNING
    ```

6) Connect to your Kubernetes Cluster

    ```shell
    gcloud container clusters get-credentials petclinic-cluster --region europe-west3 --project $PROJECT_ID
    ```

7) Create namespaces for the Petclinic application

   Create a Knative namespace for native executables on `dev` stage
    ```shell
    kubectl create namespace petclinic-native-knative-dev
    ```

   Create a Knative namespace for native images on `prod` stage
    ```shell
    kubectl create namespace petclinic-native-knative-prod
    ```

   Create a Knative namespace for JVM images on `dev` stage
    ```shell
    kubectl create namespace petclinic-jvm-knative-dev
    ```

   Create a Knative namespace for JVM images on `prod` stage
    ```shell
    kubectl create namespace petclinic-jvm-knative-prod
    ```

### Setup Knative

1) Install the service component

    ```shell
    kubectl apply --filename https://github.com/knative/serving/releases/download/v0.21.0/serving-crds.yaml
    ```

2) Install the core components of Serving

    ```shell
    kubectl apply --filename https://github.com/knative/serving/releases/download/v0.21.0/serving-core.yaml
    ```

3) Install a properly configured Istio

    ```shell
    kubectl apply --filename https://github.com/knative/net-istio/releases/download/v0.21.0/istio.yaml
    ```

4) Install the Knative Istio controller

    ```shell
    kubectl apply --filename https://github.com/knative/net-istio/releases/download/v0.21.0/net-istio.yaml
    ```

5) Configure DNS with MagicDNS (xip.io)

    ```shell
    kubectl apply --filename https://github.com/knative/serving/releases/download/v0.21.0/serving-default-domain.yaml
    ```

   You might have to wait a few minutes and retry it later if you receive a connection time out error.

6) Monitor the Knative components until all of the components show a STATUS of `Running` or `Completed:

    ```shell
    kubectl get pods --namespace knative-serving
    ```

   #### Common Knative Commands:

   Show details about all Knative Services:

    ```shell
    kubectl get ksvc --all-namespaces
    ```

   Show details about `quarkus-petclinic-rest application`

    ```shell
    kubectl get ksvc quarkus-petclinic-rest --namespace <CHOOSE NAMESPACE FROM LIST BELOW>
    ```

   **Available Namespaces:**

    - `petclinic-native-knative-dev` - Namespace for Petclinic `development` containers running as a native executable
    - `petclinic-native-knative-prod` - Namespace for Petclinic `production` containers running as a native executable
    - `petclinic-native-knative-dev` - Namespace for Petclinic `development` containers running with a Java Virtual
      Machine (JVM)
    - `petclinic-native-knative-prod` - Namespace for Petclinic `production` containers running with a Java Virtual
      Machine (JVM)

### GitHub configuration

In GitHub, you need to set up the following secrets via your repositories settings tab:

- `GCP_PROJECT_ID` - The GCP project id which was defined in `$PROJECT_ID` during the service account creation step
- `GCP_SERVICE_ACCOUNT_EMAIL` - The email from the previously created service account
- `GCP_SERVICE_ACCOUNT_CREDENTIALS` - The content from the `key.json` file that was previously created
- `GCP_DB_HOST` - The hostname or public IP address of the database server
- `GCP_DB_PORT` - The port of the database server
- `GCP_DB_DATABASE` - The database of your db server you are using for the application
- `GCP_DB_USERNAME` - The db username for your application
- `GCP_DB_PASSWORD` - The db user password for your application

In the end your secrets should look like this:

[![SETUP_GITHUB_GCP_SECRETS_00](docs/screenshots/setup-github-secrets/setup_github_gcp_secrets_00.png)](docs/screenshots/setup-github-secrets/setup_github_gcp_secrets_00.png)

## Google Cloud Run Endpoints - Development

| Application                   | URL
| ----------------------------- |:-------------:
| Spring Petclinic (JVM)        | https://spring-petclinic-rest-slim-jvm-dev-s2xflp6dzq-ey.a.run.app/petclinic/api

## Google Cloud Run Endpoints - Production

| Application                   | URL
| ----------------------------- |:-------------:
| Spring Petclinic (JVM)        | https://spring-petclinic-rest-slim-jvm-prod-s2xflp6dzq-ey.a.run.app/petclinic/api

