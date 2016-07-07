hibernate-jsonb
======================

Hibernate 4.3.x (JPA 2.1) + PostgreSQL 9.4 JSONB support example.

* Hibernate dialect with JSONB support
* Hibernate user-type for JSONB support (from a String)
* Static-metamodel & SQL generation
* Simple DAO with native JSONB query

# Pre-requisites

* JDK 7
* Maven 3.1.0 or newer

# Build and test

## PostgreSQL 9.4

Install, configure and start PostgreSQL 9.4 on ```localhost```. If you change this, you must update ```src/test/resources/META-INF/persistence.xml``` accordingly.

```
sudo su postgres
createdb dbtest
```

## Test

```
mvn clean test
```

# Generate drop-create SQL

```
mvn clean package -Pgenerate-sql
```

See ```sql/drop-create.sql```
