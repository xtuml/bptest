Here is the command-line that can be used to regenerate this matrix without overwritting the test class where the implementation changes go.

perl ~/git/mac1/bptest/src/org.xtuml.bp.test/UnitTestGenerator.pl ~/git/mac1/bridgepoint/doc-bridgepoint/notes/9761_9762_find_declarations/9761_9762_find_declarations_matrix.txt  ./OpenDeclarationTests.java  -p org.xtuml.bp.ui.text.editor.oal.opendeclaration -suite -DNO
