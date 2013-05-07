/**
 * 
 */
package com.google.typography.font.sfntly.table.cff;

import com.google.typography.font.sfntly.data.FontData;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;

/**
 * @author stuartg
 *
 */
public class Encoding extends CffSubTable {

  /**
   * Offsets to specific elements in the underlying data. These offsets are relative to the
   * start of the table or the start of sub-blocks within the table.
   */
  enum Offset {
    // header
    format(0),
    
    // format 0
    format0HeaderLength(FontData.DataSize.Card8.size() * 2),
    format0_nCodes(format.offset + FontData.DataSize.Card8.size()),
    format0_codeArray(format0_nCodes.offset + FontData.DataSize.Card8.size()),
    
    // format 1
    format1HeaderLength(FontData.DataSize.Card8.size() * 2),
    format1_nRanges(format.offset + FontData.DataSize.Card8.size()),
    format1_rangeArray(format1_nRanges.offset + FontData.DataSize.Card8.size()),
    format1_rangeSize(2 * FontData.DataSize.Card8.size()),
    
    format1_range_first(0),
    format1_range_nLeft(format1_range_first.offset + FontData.DataSize.Card8.size()),
    
    // Supplemental
    nSups(0),
    supplementArray(nSups.offset + FontData.DataSize.Card8.size()),
    supplementSize(FontData.DataSize.Card8.size() + FontData.DataSize.SID.size());

    private final int offset;

    private Offset(int offset) {
      this.offset = offset;
    }
  }


  private int format;
  private int nCodes;
  
  /**
   * @param data
   */
  private Encoding(ReadableFontData data) {
    super(data);
    initialize();
  }
  
  private void initialize() {
    
  }
  
  private static int format(ReadableFontData data) {
    // remove the high order bit which is used for indicating supplemental data
    return data.readCard8(Offset.format.offset) & 0x7f;
  }
  
  private static boolean hasSupplemental(ReadableFontData data) {
    // high order bit indicates supplemental data
    return (data.readCard8(Offset.format.offset) & 0x80) == 0x80;
  }
  
  static int encodingLength(ReadableFontData data) {
    int format = Encoding.format(data);
    boolean hasSupplemental = Encoding.hasSupplemental(data);
    
    int length = 0;
    
    if (format == 0) {
      length += Offset.format0HeaderLength.offset;
      int nCodes = data.readCard8(Offset.format0_nCodes.offset);
      length += nCodes * FontData.DataSize.Card8.size();
    } else if (format == 1) {
      length += Offset.format1HeaderLength.offset;
      int nRanges = data.readCard8(Offset.format1_nRanges.offset);
      length += nRanges * Offset.format1_rangeSize.offset;
    } else {
      // TODO(stuartg): error condition
    }
    
    if (hasSupplemental) {
      int nSups = data.readCard8(length);
      length += nSups * Offset.supplementSize.offset;
    }
    
    return length;
  }
  
  public static class Builder extends CffSubTable.Builder<Encoding> {
    //private Encoding encoding;
    
    protected Builder(ReadableFontData data) {
      super(data);
      //initialize();
    }
    
    protected Builder(WritableFontData data) {
      super(data);
      //initialize();
    }

    public static Builder createBuilder(ReadableFontData data) {
      return new Builder(data);
    }
    
    // TODO(stuartg): remove these when specific Encoding objects built
    @Override
    protected int subSerialize(WritableFontData newData) {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    protected boolean subReadyToSerialize() {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    protected int subDataSizeToSerialize() {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    protected void subDataSet() {
      // TODO Auto-generated method stub
      
    }

    @Override
    protected Encoding subBuildTable(ReadableFontData data) {
      // TODO Auto-generated method stub
      return null;
    }

  }
}