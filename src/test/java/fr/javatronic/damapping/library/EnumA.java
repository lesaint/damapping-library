package fr.javatronic.damapping.library;

/**
* EnumA -
*
* @author Sébastien Lesaint
*/
enum EnumA {
  VALUE_1, VALUE_2, VALUE_3;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
