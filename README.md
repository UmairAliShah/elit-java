# ELIT for Java

This project provides the Java SDK and components for the [Evolution of Language and Information Technology (ELIT)](https://elit.cloud) platform.
It is under the [Apache 2](http://www.apache.org/licenses/LICENSE-2.0) license and currently led by the [Emory NLP](http://nlp.mathcs.emory.edu) research group.

* Latest release: [0.0.2](https://search.maven.org/#artifactdetails|cloud.elit|elit|0.0.2|pom) ([release notes](md/release.md)).
* [ELIT SDK for Python](https://github.com/elitcloud/elit-sdk-python).

## Installation

Add the following dependency to the `pom.xml` in your maven project.

```xml
<dependency>
    <groupId>cloud.elit</groupId>
    <artifactId>elit-sdk</artifactId>
    <version>0.0.2</version>
</dependency>
```

## Web API

The following code makes a HTTP request to retrieve NLP output for the input string from all components in [spaCy](https://spacy.io).

```java
import cloud.elit.sdk.api.Client;
import cloud.elit.sdk.api.TaskRequest;
import cloud.elit.sdk.nlp.structure.Document;
import cloud.elit.sdk.nlp.structure.util.Tools;
import cloud.elit.sdk.nlp.structure.util.Fields;

public class DecodeWebAPITest {
    static public void main(String[] args) {
        Client api = new Client();
        String input = "Hello World! Welcome to ELIT.";
        TaskRequest r = new TaskRequest(input, Fields.ALL, Tools.SPACY);

        String output = api.decode(r);
        System.out.println(output);
    }
}
```

The web-API then retrieves the NLP output in JSON as follows:

```json
{
  "output": [{
    "sid": 0,
    "tok": ["Hello", "World", "!"],
    "lem": ["hello", "world", "!"],
    "pos": ["UH", "NN", "."],
    "ner": [],
    "dep": [[1, "intj"], [-1, "ROOT"], [1, "punct"]]
  },
  {
    "sid": 1,
    "tok": ["Welcome", "to", "ELIT", "."],
    "lem": ["welcome", "to", "elit", "."],
    "pos": ["VBP", "IN", "NN", "."],
    "ner": [[2, 3, "ORG"]],
    "dep": [[-1, "ROOT"], [2, "aux"], [0, "xcomp"], [0, "punct"]]
  }],
  "pipeline": {
    "dep": "spacy",
    "lem": "spacy",
    "ner": "spacy",
    "pos": "spacy",
    "tok": "spacy"
}
```

Our SDK provides a convenient wrapper class to read the JSON output and convert it into a structure.

```java
import cloud.elit.sdk.api.Client;
import cloud.elit.sdk.api.TaskRequest;
import cloud.elit.sdk.nlp.structure.Document;
import cloud.elit.sdk.nlp.structure.util.Tools;
import cloud.elit.sdk.nlp.structure.util.Fields;
import cloud.elit.sdk.nlp.structure.Document;
import cloud.elit.sdk.nlp.structure.Sentence;
import cloud.elit.sdk.nlp.structure.node.NLPNode;

public class DecodeWebAPITest {
    static public void main(String[] args) {
        Client api = new Client();
        String input = "Hello World! Welcome to ELIT.";
        TaskRequest r = new TaskRequest(input, Fields.ALL, Tools.SPACY);

        String output = api.decode(r);
        Document doc = new Document(output);

        for (Sentence sen : doc) {
            for (NLPNode node : sen)
                System.out.println(String.format("%s(%s, %s)", 
                        node.getDependencyLabel(), 
                        node.getToken(), 
                        node.getParent().getToken()));
            System.out.println();
        }
    }
}
```

The above code generates the following output:

```
intj(Hello, World)
ROOT(World, @#r$%)
punct(!, World)

ROOT(Welcome, @#r$%)
aux(to, ELIT)
xcomp(ELIT, Welcome)
punct(., Welcome)
```




* [`Fields`](../../tree/master/elit-sdk/src/main/java/cloud/elit/sdk/nlp/structure/util/Fields.java): 
* [`Tools`](../../tree/master/elit-sdk/src/main/java/cloud/elit/sdk/nlp/structure/util/Tools.java)
