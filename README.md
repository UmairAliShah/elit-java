# ELIT for Java

This project provides the Java SDK and components for the [Evolution of Language and Information Technology (ELIT)](https://elit.cloud) platform.
It is under the [Apache 2](http://www.apache.org/licenses/LICENSE-2.0) license and currently led by the [Emory NLP](http://nlp.mathcs.emory.edu) research group.

* Latest release: [0.0.2](https://search.maven.org/#artifactdetails|cloud.elit|elit|0.0.2|pom) ([release notes](md/release.md)).
* [Javadoc](https://elitcloud.github.io/elit-java/index.html).


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

* Replace `Fields.ALL` with `Fields.TOK`, `Fields.LEM`, `Fields.POS`, `Fields.NER`, or `Fields.DEP` if you wish to perform the NLP pipeline only up to tokenization, lemmatization, part-of-speech tagging, named entity recognition, or dependency parsing, respectively.
* Replace `Tools.SPACY` with `Tools.ELIT` or `Tools.NLP4J` if you want to use components provided by [ELIT](https://elit.cloud) or [NLP4J](https://emorynlp.github.io/nlp4j/) instead.

```java
import cloud.elit.sdk.api.Client;
import cloud.elit.sdk.api.TaskRequest;
import Document;
import Tools;
import Fields;

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
    "tok": "spacy"}
}
```

Our SDK provides a convenient wrapper class to read the JSON output and convert it into a structure (see the [Javadoc](https://elitcloud.github.io/elit-java/index.html) for more details).

```java
import cloud.elit.sdk.api.Client;
import cloud.elit.sdk.api.TaskRequest;
import Document;
import Tools;
import Fields;
import Document;
import Sentence;
import NLPNode;

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

Our SDK also allows you to create an NLP pipeline consisting of multiple tools.
The following code makes a request specifying ELIT for tokenization, NLP4J for part-of-speech tagging and spaCy for dependency parsing.

```java
TaskRequest r = new TaskRequest(input, Fields.DEP, Tools.SPACY);
r.setDependencies(new TaskDependency(Fields.TOK, Tools.ELIT), new TaskDependency(Fields.POS, Tools.NLP4J));
```

