package com.jogamp.opengl.test.bugs;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.test.junit.jogl.demos.es2.LandscapeES2;

/**
 * Difference to orig. Bug735Inv0AppletAWT:
 * <pre>
 *   - Use GLEventListener
 *   - Add GLEventListener to GLAutoDrawable
 *   - Use GLAutoDrawable.display() instead of GLAutoDrawable.invoke(true, GLRunnable { init / render })
 *   - Removed MANUAL_FRAME_HANDLING, obsolete due to GLAutoDrawable/GLEventListener 
 * </pre>
 * OSX Results:
 * <pre>
 *   - Visible content
 *   - Stuttering, non-fluent and slow animation
 * </pre>
 */
@SuppressWarnings("serial")
public class Bug735Inv2AppletAWT extends Applet implements Runnable {
  static public int AWT  = 0;
  static public int NEWT = 1;
  
  static public int APPLET_WIDTH  = 500;
  static public int APPLET_HEIGHT = 290;
  static public int TARGET_FPS    = 120;
  static public int TOOLKIT       = NEWT;
  static public boolean IGNORE_AWT_REPAINT = false;
  static public boolean USE_ECT = false;
  static public int SWAP_INTERVAL = 0;
  
  //////////////////////////////////////////////////////////////////////////////
  
  static private Frame frame;
  static private Bug735Inv2AppletAWT applet;
  private GLCanvas awtCanvas;
  private GLWindow newtWindow;
  private GLAutoDrawable glad;
  private NewtCanvasAWT newtCanvas;
  private GLEventListener demo;
  
  private int width;
  private int height;
  private Thread thread;
  
  private long frameRatePeriod = 1000000000L / TARGET_FPS;
  private int frameCount;
  
  public void init() {
    setSize(APPLET_WIDTH, APPLET_HEIGHT);
    setPreferredSize(new Dimension(APPLET_WIDTH, APPLET_HEIGHT));
    width = APPLET_WIDTH;
    height = APPLET_HEIGHT;
    initGL();
  }
  
  public void start() {
    initDraw();
    thread = new Thread(this, "Animation Thread");
    thread.start();    
  }
  
  public void run() {    
    int noDelays = 0;
    // Number of frames with a delay of 0 ms before the
    // animation thread yields to other running threads.
    final int NO_DELAYS_PER_YIELD = 15;
    final int TIMEOUT_SECONDS = 2;
    
    long beforeTime = System.nanoTime();
    long overSleepTime = 0L;
    
    frameCount = 1;
    while (Thread.currentThread() == thread) {
      if (frameCount == 1) {
        EventQueue.invokeLater(new Runnable() {
          public void run() {
            requestFocusInWindow();
          }
        });
        if( USE_ECT ) {
            glad.setExclusiveContextThread(thread);
        }
      }      
      final CountDownLatch latch = new CountDownLatch(1);
      requestDraw(latch);
      try {
        latch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
            
      long afterTime = System.nanoTime();
      long timeDiff = afterTime - beforeTime;
      long sleepTime = (frameRatePeriod - timeDiff) - overSleepTime;      
      if (sleepTime > 0) {  // some time left in this cycle
        try {
          Thread.sleep(sleepTime / 1000000L, (int) (sleepTime % 1000000L));
          noDelays = 0;  // Got some sleep, not delaying anymore
        } catch (InterruptedException ex) { }
        overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
      } else {    // sleepTime <= 0; the frame took longer than the period
        overSleepTime = 0L;
        noDelays++;
        if (noDelays > NO_DELAYS_PER_YIELD) {
          Thread.yield();   // give another thread a chance to run
          noDelays = 0;
        }
      }      
      beforeTime = System.nanoTime();
    }
  }
  
  public void requestDraw(CountDownLatch latch) {
    glad.display();

    if (latch != null) {
      latch.countDown();
    }    
  }
  
  private void initGL() {
    GLProfile profile = GLProfile.getDefault();
    GLCapabilities caps = new GLCapabilities(profile);
    caps.setBackgroundOpaque(true);
    caps.setOnscreen(true);
    caps.setSampleBuffers(false);
    
    if (TOOLKIT == AWT) {
      awtCanvas = new GLCanvas(caps);
      awtCanvas.setBounds(0, 0, applet.width, applet.height);
      awtCanvas.setBackground(new Color(0xFFCCCCCC, true));
      awtCanvas.setFocusable(true); 
      
      applet.setLayout(new BorderLayout());
      applet.add(awtCanvas, BorderLayout.CENTER);
      
      if (IGNORE_AWT_REPAINT) {
          awtCanvas.setIgnoreRepaint(true);
      }
      glad = awtCanvas;
    } else if (TOOLKIT == NEWT) {      
      newtWindow = GLWindow.create(caps);
      newtCanvas = new NewtCanvasAWT(newtWindow);
      newtCanvas.setBounds(0, 0, applet.width, applet.height);
      newtCanvas.setBackground(new Color(0xFFCCCCCC, true));
      newtCanvas.setFocusable(true);

      applet.setLayout(new BorderLayout());
      applet.add(newtCanvas, BorderLayout.CENTER);
      
      if (IGNORE_AWT_REPAINT) {
        newtCanvas.setIgnoreRepaint(true);
      }
      glad = newtWindow;
    }
    
    demo = new LandscapeES2(SWAP_INTERVAL);
    glad.addGLEventListener(demo);
  }
  
  private void initDraw() {
    if (TOOLKIT == AWT) {
      awtCanvas.setVisible(true);
      // Force the realization
      awtCanvas.display();
      if (awtCanvas.getDelegatedDrawable().isRealized()) {
        // Request the focus here as it cannot work when the window is not visible
        awtCanvas.requestFocus();
      }      
    } else if (TOOLKIT == NEWT) {
      newtCanvas.setVisible(true);
      // Force the realization
      newtWindow.display();
      if (newtWindow.isRealized()) {
        // Request the focus here as it cannot work when the window is not visible
        newtCanvas.requestFocus();
      }
    }
  }
  
  static public void main(String[] args) {    
    GraphicsEnvironment environment = 
        GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice displayDevice = environment.getDefaultScreenDevice();

    frame = new Frame(displayDevice.getDefaultConfiguration());
    frame.setBackground(new Color(0xCC, 0xCC, 0xCC));
    frame.setTitle("TestBug735Inv2AppletAWT");
    
    try {
      Class<?> c = Thread.currentThread().getContextClassLoader().
          loadClass(Bug735Inv2AppletAWT.class.getName());
      applet = (Bug735Inv2AppletAWT) c.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }    
    
    frame.setLayout(null);
    frame.add(applet);
    frame.pack();
    frame.setResizable(false);
    
    applet.init();
    
    Insets insets = frame.getInsets();
    int windowW = applet.width + insets.left + insets.right;
    int windowH = applet.height + insets.top + insets.bottom;
    frame.setSize(windowW, windowH);    
    
    Rectangle screenRect = displayDevice.getDefaultConfiguration().getBounds();    
    frame.setLocation(screenRect.x + (screenRect.width - applet.width) / 2,
        screenRect.y + (screenRect.height - applet.height) / 2);    
    
    int usableWindowH = windowH - insets.top - insets.bottom;
    applet.setBounds((windowW - applet.width)/2,
                     insets.top + (usableWindowH - applet.height)/2,
                     applet.width, applet.height);
    
    // This allows to close the frame.
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
        
    frame.setVisible(true);
    applet.start();    
  }
}
