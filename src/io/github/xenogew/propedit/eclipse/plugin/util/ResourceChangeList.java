/**
 * 
 */
package io.github.xenogew.propedit.eclipse.plugin.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 *
 */
public class ResourceChangeList implements Iterable<ResourceChange> {

  private List<ResourceChange> list = new ArrayList<>();

  public void add(ResourceChange rc) {
    boolean addDoneFlg = false;
    List<ResourceChange> newList = new ArrayList<>();
    for (ResourceChange rcInList : list) {
      if (rc.getType() == ResourceChange.PROPERTIES_CHANGE) {
        if (rcInList.getProject().getFullPath().toPortableString()
            .equals(rc.getProject().getFullPath().toPortableString())) {
          newList.add(rcInList);
          addDoneFlg = true;
          continue;
        }
      } else {
        if (rcInList.getProject().getFullPath().toPortableString()
            .equals(rc.getProject().getFullPath().toPortableString())) {
          if (rcInList.getType() == ResourceChange.PROPERTIES_CHANGE) {
            newList.remove(rcInList);
            newList.add(rc);
            addDoneFlg = true;
            continue;
          } else {
            newList.add(rcInList);
            addDoneFlg = true;
            continue;
          }
        }
      }
      newList.add(rcInList);
    }
    if (!addDoneFlg)
      newList.add(rc);
    list = newList;
  }

  public void addAll(ResourceChangeList rcList) {
    for (ResourceChange rc : rcList.list) {
      add(rc);
    }
  }

  @Override
  public Iterator<ResourceChange> iterator() {
    return list.iterator();
  }

  public int size() {
    return list.size();
  }

}
