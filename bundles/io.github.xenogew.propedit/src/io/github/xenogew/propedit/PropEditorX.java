package io.github.xenogew.propedit;

import java.util.ResourceBundle;
import org.eclipse.swt.widgets.Display;

/**
 * This class is main class of application.
 */
public class PropEditorX {

  private static ResourceBundle res = ResourceBundle.getBundle("io.github.xenogew.propedit.lang");

  public static String VERSION = res.getString("version");

  public static String COPYRIGHT = res.getString("2004_Sou Miyazaki_All");

  public static void main(String[] args) {
    Display display = new Display();
    PropEditorXFrameSWT frame = new PropEditorXFrameSWT(display);
    frame.open();

    while (!frame.getShell().isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();
  }

  public static void showCopyRight() {
    System.out.println("/*****************************************");
    System.out.println(res.getString("PropEditorX"));
    System.out.println(res.getString("Version_") + PropEditorX.VERSION);
    System.out.println(PropEditorX.COPYRIGHT);
    System.out.println("*****************************************/");
  }

  public static String getI18nProperty(String key) {
    return res.getString(key);
  }
}
