# DDR Conversion

DDR conversion generates the [deep dependency graphs](https://github.com/emorynlp/ddr) from the Penn Treebank style constituency trees.
The conversion tool is written in Java and developed by [Emory NLP](http://nlp.mathcs.emory.edu) as a part of the [ELIT](https://elit.cloud) project.

## Installation

Add the following dependency to your maven project:

```
<dependency>
    <groupId>cloud.elit</groupId>
    <artifactId>elit-ddr</artifactId>
    <version>0.0.4</version>
</dependency>
```

## Command Line

Run the following command:

```
java cloud.elit.ddr.bin.DDRConvert -i <filepath> [ -r -n -pe <string> -oe <string>]
```
   
* `-i`: the path to the parse file or a directory containing the parse files to convert.
* `-r`: if set, process all files with the extension in the subdirectories of the input directory recursively.
* `-n`: if set, normalize the parse trees before the conversion.
* `-pe`: the extension of the parse files; required if the input path indicates a directory (default: `parse`).
* `-oe`: the extension of the output files (default: `tsv`).

The following command reads constituency trees from [relcl.parse](https://github.com/elitcloud/elit-java/blob/master/elit-ddr/src/test/resources/conversion/english/relcl.parse) and generates deep dependency graphs to [relcl.parse.tsv](https://github.com/elitcloud/elit-java/blob/master/elit-ddr/src/test/resources/conversion/english/tsv):

```bash
java cloud.elit.ddr.bin.DDRConvert -i relcl.parse -oe tsv
```

The following command reads all parse files under the [english](https://github.com/elitcloud/elit-java/blob/master/elit-ddr/src/test/resources/conversion/english) directory (`*.parse`) and generates the TSV files accordingly (`*.tsv`):

```bash
java cloud.elit.ddr.bin.DDRConvert -i english -pe parse -oe tsv
```

## Java API

The following code reads constituency trees from [relcl.parse](https://github.com/elitcloud/elit-java/blob/master/elit-ddr/src/test/resources/conversion/english/relcl.parse) and generates deep dependency graphs to [relcl.tsv](https://github.com/elitcloud/elit-java/blob/master/elit-ddr/src/test/resources/conversion/english/relcl.tsv):

```java
import cloud.elit.ddr.conversion.EnglishC2DConverter;
import cloud.elit.ddr.conversion.C2DConverter;
import cloud.elit.ddr.util.Language;

public class DDRConvertDemo {
    public static void main(String[] args) {
        final String parseFile = "relcl.parse";
        final String tsvFile = "relcl.tsv";
        C2DConverter converter = new EnglishC2DConverter();
        DDRConvert ddr = new DDRConvert();
        ddr.convert(converter, Language.ENGLISH, parseFile, tsvFile, false);
    }
}
```

## Format

The converted deep dependency graphs are represented in the Tab Separated Values format (TSV), where each column represents a distinct field.
Sentences are delimited by empty lines.
The semantic tags are indicated in the `feats` column with the key, `sem`.

```tsv
1	I	I	PRP	_	2	nsbj	_
2	have	have	VBP	_	0	root	_
3	homework	homework	NN	_	2	obj	_
4	to	to	TO	_	5	aux	_
5	do	do	VB	_	3	acl	_

1	The	the	DT	_	2	det	_
2	homework	homework	NN	_	7	nsbj	3:obj
3	assigned	assign	VBN	_	2	acl	_
4	by	by	IN	_	5	case	_
5	John	john	NNS	_	3	nsbj	_
6	was	be	VBD	_	7	cop	_
7	hard	hard	JJ	_	0	root	_

1	I	I	PRP	_	2	nsbj	_
2	found	find	VBD	_	0	root	_
3	an	an	DT	_	4	det	_
4	evidence	evidence	NN	_	2	obj	_
5	that	that	IN	_	7	mark	_
6	John	john	NN	_	7	nsbj	_
7	studied	study	VBD	_	4	acl	_

```

* `id`: current token ID (starting at 1).
* `form`: word form.
* `lemma`: lemma.
* `pos`: part-of-speech tag.
* `feats`: extra features; different features are delimited by `|`, keys and values are delimited by `=` (`_` indicates no feature).
* `headId`: head token ID.
* `deprel`: dependency label.
* `sheads`: secondary heads; multiple heads are delimited by `|` (`_` indicates no secondary head).
* `nament`: named entity tags in the `BILOU` notation if available.

## Visualization

Use our web-based tool to visualize deep dependency graphs: https://emorynlp.github.io/ddr/viz/