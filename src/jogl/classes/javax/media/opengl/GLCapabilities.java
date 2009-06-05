/*
 * Copyright (c) 2003-2009 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN
 * MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed or intended for use
 * in the design, construction, operation or maintenance of any nuclear
 * facility.
 * 
 * Sun gratefully acknowledges that this software was originally authored
 * and developed by Kenneth Bradley Russell and Christopher John Kline.
 */

package javax.media.opengl;

import javax.media.nativewindow.Capabilities;

/** Specifies a set of OpenGL capabilities that a rendering context
    must support, such as color depth and whether stereo is enabled.
    It currently contains the minimal number of routines which allow
    configuration on all supported window systems. */

public class GLCapabilities extends Capabilities implements Cloneable {
  private GLProfile glProfile = null;
  private boolean doubleBuffered = true;
  private boolean stereo         = false;
  private boolean hardwareAccelerated = true;
  private int     depthBits      = 24;
  private int     stencilBits    = 0;
  private int     accumRedBits   = 0;
  private int     accumGreenBits = 0;
  private int     accumBlueBits  = 0;
  private int     accumAlphaBits = 0;
  // Shift bits from PIXELFORMATDESCRIPTOR not present because they
  // are unlikely to be supported on Windows anyway

  // Support for full-scene antialiasing (FSAA)
  private boolean sampleBuffers = false;
  private int     numSamples    = 2;

  // Support for transparent windows containing OpenGL content
  // (currently only has an effect on Mac OS X)
  private boolean backgroundOpaque = true;

  // Bits for pbuffer creation
  private boolean pbufferFloatingPointBuffers;
  private boolean pbufferRenderToTexture;
  private boolean pbufferRenderToTextureRectangle;

  /** Creates a GLCapabilities object. All attributes are in a default state.
    * @param glp GLProfile, or null for the default GLProfile
    */
  public GLCapabilities(GLProfile glp) {
      glProfile = (null!=glp)?glp:GLProfile.GetProfileDefault();
  }

  public Object clone() {
    try {
      return super.clone();
    } catch (RuntimeException e) {
      throw new GLException(e);
    }
  }

  /** Returns the GL profile you desire or used by the drawable. */
  public GLProfile getGLProfile() {
    return glProfile;
  }

  /** Sets the GL profile you desire */
  public void setGLProfile(GLProfile profile) {
    glProfile=profile;
  }

  /** Indicates whether double-buffering is enabled. */
  public boolean getDoubleBuffered() {
    return doubleBuffered;
  }

  /** Enables or disables double buffering. */
  public void setDoubleBuffered(boolean onOrOff) {
    doubleBuffered = onOrOff;
  }

  /** Indicates whether stereo is enabled. */
  public boolean getStereo() {
    return stereo;
  }
  
  /** Enables or disables stereo viewing. */
  public void setStereo(boolean onOrOff) {
    stereo = onOrOff;
  }

  /** Indicates whether hardware acceleration is enabled. */
  public boolean getHardwareAccelerated() {
    return hardwareAccelerated;
  }
  
  /** Enables or disables hardware acceleration. */
  public void setHardwareAccelerated(boolean onOrOff) {
    hardwareAccelerated = onOrOff;
  }

  /** Returns the number of bits requested for the depth buffer. */
  public int getDepthBits() {
    return depthBits;
  }

  /** Sets the number of bits requested for the depth buffer. */
  public void setDepthBits(int depthBits) {
    this.depthBits = depthBits;
  }
  
  /** Returns the number of bits requested for the stencil buffer. */
  public int getStencilBits() {
    return stencilBits;
  }

  /** Sets the number of bits requested for the stencil buffer. */
  public void setStencilBits(int stencilBits) {
    this.stencilBits = stencilBits;
  }
  
  /** Returns the number of bits requested for the accumulation
      buffer's red component. On some systems only the accumulation
      buffer depth, which is the sum of the red, green, and blue bits,
      is considered. */
  public int getAccumRedBits() {
    return accumRedBits;
  }

  /** Sets the number of bits requested for the accumulation buffer's
      red component. On some systems only the accumulation buffer
      depth, which is the sum of the red, green, and blue bits, is
      considered. */
  public void setAccumRedBits(int accumRedBits) {
    this.accumRedBits = accumRedBits;
  }

  /** Returns the number of bits requested for the accumulation
      buffer's green component. On some systems only the accumulation
      buffer depth, which is the sum of the red, green, and blue bits,
      is considered. */
  public int getAccumGreenBits() {
    return accumGreenBits;
  }

  /** Sets the number of bits requested for the accumulation buffer's
      green component. On some systems only the accumulation buffer
      depth, which is the sum of the red, green, and blue bits, is
      considered. */
  public void setAccumGreenBits(int accumGreenBits) {
    this.accumGreenBits = accumGreenBits;
  }

  /** Returns the number of bits requested for the accumulation
      buffer's blue component. On some systems only the accumulation
      buffer depth, which is the sum of the red, green, and blue bits,
      is considered. */
  public int getAccumBlueBits() {
    return accumBlueBits;
  }

  /** Sets the number of bits requested for the accumulation buffer's
      blue component. On some systems only the accumulation buffer
      depth, which is the sum of the red, green, and blue bits, is
      considered. */
  public void setAccumBlueBits(int accumBlueBits) {
    this.accumBlueBits = accumBlueBits;
  }

  /** Returns the number of bits requested for the accumulation
      buffer's alpha component. On some systems only the accumulation
      buffer depth, which is the sum of the red, green, and blue bits,
      is considered. */
  public int getAccumAlphaBits() {
    return accumAlphaBits;
  }

  /** Sets number of bits requested for accumulation buffer's alpha
      component. On some systems only the accumulation buffer depth,
      which is the sum of the red, green, and blue bits, is
      considered. */
  public void setAccumAlphaBits(int accumAlphaBits) {
    this.accumAlphaBits = accumAlphaBits;
  }

  /** Indicates whether sample buffers for full-scene antialiasing
      (FSAA) should be allocated for this drawable. Defaults to
      false. */
  public void setSampleBuffers(boolean onOrOff) {
    sampleBuffers = onOrOff;
  }

  /** Returns whether sample buffers for full-scene antialiasing
      (FSAA) should be allocated for this drawable. Defaults to
      false. */
  public boolean getSampleBuffers() {
    return sampleBuffers;
  }

  /** If sample buffers are enabled, indicates the number of buffers
      to be allocated. Defaults to 2. */
  public void setNumSamples(int numSamples) {
    this.numSamples = numSamples;
  }

  /** Returns the number of sample buffers to be allocated if sample
      buffers are enabled. Defaults to 2. */
  public int getNumSamples() {
    return numSamples;
  }

  /** For pbuffers only, indicates whether floating-point buffers
      should be used if available. Defaults to false. */
  public void setPbufferFloatingPointBuffers(boolean onOrOff) {
    pbufferFloatingPointBuffers = onOrOff;
  }

  /** For pbuffers only, returns whether floating-point buffers should
      be used if available. Defaults to false. */
  public boolean getPbufferFloatingPointBuffers() {
    return pbufferFloatingPointBuffers;
  }

  /** For pbuffers only, indicates whether the render-to-texture
      extension should be used if available.  Defaults to false. */
  public void setPbufferRenderToTexture(boolean onOrOff) {
    pbufferRenderToTexture = onOrOff;
  }

  /** For pbuffers only, returns whether the render-to-texture
      extension should be used if available.  Defaults to false. */
  public boolean getPbufferRenderToTexture() {
    return pbufferRenderToTexture;
  }

  /** For pbuffers only, indicates whether the
      render-to-texture-rectangle extension should be used if
      available. Defaults to false. */
  public void setPbufferRenderToTextureRectangle(boolean onOrOff) {
    pbufferRenderToTextureRectangle = onOrOff;
  }

  /** For pbuffers only, returns whether the render-to-texture
      extension should be used. Defaults to false. */
  public boolean getPbufferRenderToTextureRectangle() {
    return pbufferRenderToTextureRectangle;
  }

  /** For on-screen OpenGL contexts on some platforms, sets whether
      the background of the context should be considered opaque. On
      supported platforms, setting this to false, in conjunction with
      other changes at the window toolkit level, can allow
      hardware-accelerated OpenGL content inside of windows of
      arbitrary shape. To achieve this effect it is necessary to use
      an OpenGL clear color with an alpha less than 1.0. The default
      value for this flag is <code>true</code>; setting it to false
      may incur a certain performance penalty, so it is not
      recommended to arbitrarily set it to false. */
  public void setBackgroundOpaque(boolean opaque) {
    backgroundOpaque = opaque;
  }

  /** Indicates whether the background of this OpenGL context should
      be considered opaque. Defaults to true.

      @see #setBackgroundOpaque
  */
  public boolean isBackgroundOpaque() {
    return backgroundOpaque;
  }

  /** Returns a textual representation of this GLCapabilities
      object. */ 
  public String toString() {
    return getClass().toString()+"[" +
        super.toString()+
        ", GL profile: " + glProfile +
        ", DoubleBuffered: " + doubleBuffered +
	    ", Stereo: " + stereo + 
        ", HardwareAccelerated: " + hardwareAccelerated +
	    ", DepthBits: " + depthBits +
	    ", StencilBits: " + stencilBits +
	    ", Red Accum: " + accumRedBits +
	    ", Green Accum: " + accumGreenBits +
	    ", Blue Accum: " + accumBlueBits +
	    ", Alpha Accum: " + accumAlphaBits +
        ", Multisample: " + sampleBuffers +
        ", Num samples: "+(sampleBuffers ? numSamples : 0) +
        ", Opaque: " + backgroundOpaque +
        ", PBuffer-FloatingPointBuffers: "+pbufferFloatingPointBuffers+
        ", PBuffer-RenderToTexture: "+pbufferRenderToTexture+
        ", PBuffer-RenderToTextureRectangle: "+pbufferRenderToTextureRectangle+
	    " ]";
  }
}
