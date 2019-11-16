package de.lalaland.core.utils.nbtapi;

import de.lalaland.core.utils.nbtapi.nmsitemutils.nmsmappings.ClassWrapper;
import de.lalaland.core.utils.nbtapi.nmsitemutils.nmsmappings.ReflectionMethod;
import java.lang.reflect.InvocationTargetException;

/**
 * String implementation for NBTLists
 *
 * @author tr7zw
 */
public class NBTStringList extends NBTList<String> {

  protected NBTStringList(NBTCompound owner, String name, NBTType type, Object list) {
    super(owner, name, type, list);
  }

  @Override
  public String get(int index) {
    try {
      return (String) ReflectionMethod.LIST_GET_STRING.run(listObject, index);
    } catch (Exception ex) {
      throw new NbtApiException(ex);
    }
  }

  @Override
  protected Object asTag(String object) {
    try {
      return ClassWrapper.NMS_NBTTAGSTRING.getClazz().getConstructor(String.class)
          .newInstance(object);
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | NoSuchMethodException | SecurityException e) {
      throw new NbtApiException("Error while wrapping the Oject " + object + " to it's NMS object!",
          e);
    }
  }

}
