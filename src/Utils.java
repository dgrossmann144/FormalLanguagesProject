import java.util.ArrayList;
import java.util.Arrays;

public class Utils {
   public static int findMatchingParen(String regex, int startingParen) {
      int parenCount = 1;
      for(int i = startingParen + 1; i < regex.length(); i++) {
         if(regex.charAt(i) == '(') {
            parenCount++;
         } else if(regex.charAt(i) == ')') {
            parenCount--;
            if(parenCount == 0) {
               return i;
            }
         }
      }
      return -1;
   }
   
   public static ArrayList<Integer> removeDuplicates(ArrayList<Integer> list) {
      ArrayList<Integer> result = new ArrayList<Integer>();
      for(int i = 0; i < list.size(); i++) {
         if(!result.contains(list.get(i))) {
            result.add(list.get(i));
         }
      }
      return result;
   }
   
   public static ArrayList<Integer> sort(ArrayList<Integer> list) {
      Object[] items = list.toArray();
      Arrays.sort(items);
      ArrayList<Integer> result = new ArrayList<Integer>();
      for(int i = 0; i < items.length; i++) {
         result.add((Integer) items[i]);
      }
      return result;
   }
   
//   public static boolean compareArrayLists(ArrayList<Integer> list1, ArrayList<Integer> list2) {
//      if(list1.size() == list2.size()) {
//         for(int i = 0; i < list1.size(); i++) {
//            if(list1.get(i) != list2.get(i)) {
//               return false;
//            }
//         }
//         return true;
//      }
//      return false;
//   }
}
