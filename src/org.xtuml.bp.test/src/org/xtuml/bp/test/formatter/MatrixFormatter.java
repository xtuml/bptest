/*---------------------------------------------------------------------------*/
/*
/* Matrix Formatter Utility
/*
/* This utility formats test matrices, aligning all the rows and columns
/* neatly. The header of the matrix including the degrees of freedom section
/* is untouched. Anything after a line containing 'Matrix:' will be formatted
/* as the matrix itself.
/*
/* Build:
/*     javac MatrixFormatter.java
/*
/* Usage:
/*     java MatrixFormatter <input files>
/*
/*---------------------------------------------------------------------------*/
package org.xtuml.bp.test.formatter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class MatrixFormatter {

  private String file;
  private FileReader input;
  private PrintStream output;

  private String matrixPrefix;
  private Matrix matrix;

  public MatrixFormatter( String file ) {
	this.file = file;
    input = null;
    output = null;
    matrixPrefix = "";
    matrix = null;
  }

  public void format() throws IOException {
    // setup input stream
    try {
      input = new FileReader( file );
    } catch ( FileNotFoundException e ) {
      System.err.println( "No input file found." );
      System.exit( 1 );
    }
    // read up to the matrix
    LineNumberReader reader = new LineNumberReader( input );
    String lineText = reader.readLine();
    while ( null != lineText && !lineText.contains( "Matrix:" ) ) {
      matrixPrefix += lineText + "\n";
      lineText = reader.readLine();
    }
    matrixPrefix += "Matrix:\n";
    // create the matrix and header
    lineText = reader.readLine();
    if ( null != lineText ) {
      matrix = new Matrix( lineText.trim().split( "\\s+" ).length );
      matrix.addHeader( lineText, reader.getLineNumber() );
    }
    // add the lines to the matrix
    lineText = reader.readLine();
    while ( null != lineText && 0 != lineText.length() ) {
      if ( !"".equals( lineText.trim() ) ) matrix.addRow( lineText, reader.getLineNumber() );
      lineText = reader.readLine();
    }
    // close the input file
    if ( null != input ) input.close();
    input = null;
  }

  public void output() {
    // setup output stream
    try {
      output = new PrintStream( file );
    } catch ( FileNotFoundException e ) {
      System.err.println( "No output file could be created." );
      System.exit( 2 );
    } catch ( SecurityException e ) {
      System.err.println( "Write access denied." );
      System.exit( 3 );
    }
    // ouptut the matrix
    if ( null != matrix && null != output ) {
      output.print( matrixPrefix );
      matrix.print( output );
      output.flush();
    }
    // close the output file
    if ( null != output ) output.close();
    output = null;
  }

  private class Matrix {

    private List<String[]> matrix;
    private int numColumns;

    public Matrix( int numColumns ) {
      matrix = new ArrayList<String[]>();
      this.numColumns = numColumns;
    }

    public void addHeader( String row, int lineNum ) {
      addRow( row, lineNum, true );
    }

    public void addRow( String row, int lineNum ) {
      addRow( row, lineNum, false );
    }

    private void addRow( String row, int lineNum, boolean isHeader ) {
      String[] items = row.split( "\\s+" );
      String[] matrixRow = new String[ numColumns + 1 ]; // include extra spot for the side row heading
      int i = 0;
      if ( isHeader ) matrixRow[i++] = "";
      for ( ; i < numColumns + 1; i++ ) {
        try {
          matrixRow[i] = items[i].trim();
        }
        catch ( ArrayIndexOutOfBoundsException e ) {
          System.err.printf( "Array row with wrong number of elements. Line number: %d\n", lineNum );
          System.exit( 4 );
        }
      }
      matrix.add( matrixRow );
    }

    public void print( PrintStream out ) {
      int[] columnWidths = new int[ numColumns + 1 ];
      for ( int i = 0; i < numColumns + 1; i++ ) columnWidths[i] = -1;
      for ( String[] row : matrix ) {
        for ( int j = 0; j < row.length; j++ ) {
          out.print( row[j] );
          if ( -1 == columnWidths[j] ) columnWidths[j] = calculateColumnWidth( j );
          int remainingSpaces = columnWidths[j] - row[j].length();
          for ( int i = 0; i < remainingSpaces; i++ ) out.print( " " );
        }
        out.print( "\n" );
      }
    }

    private int calculateColumnWidth( int columnNumber ) {
      int currentColumnWidth = -1;
      for ( String[] row : matrix ) {
        for ( int j = 0; j < row.length; j++ ) {
          if ( j == columnNumber && row[j].length() > currentColumnWidth ) currentColumnWidth = row[j].length();
        }
      }
      return currentColumnWidth + 2; // column width is 2 spaces bigger than the longest item for padding
    }

  }

  public static void main( String[] args ) {
    if ( args.length > 0 ) {
      for ( int i = 0; i < args.length; i++ ) {
        MatrixFormatter mf = new MatrixFormatter( args[i] );
        try {
          mf.format();
          mf.output();
          System.out.printf( "Formatted: %s\n", args[i] );
        }
        catch ( IOException e ) {
          System.err.println( "IO exception in processing." );
          System.exit( 5 );
        }
      }
    }
    else {
      System.err.println( "Usage:\n\tjava MatrixFormatter <input files>" );
    }
  }

}
