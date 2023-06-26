package kw.test.file;

public class EmoApp {
   public static String[] active = {
           getEmo(0x1F5E3)+"Knock knock! Here is Art Puzzle with lots of fun!"+getEmo(0x1F334),
           getEmo(0x2615)+"Take a break! Your unfinished puzzle is waiting for you!",
           getEmo(0x1F9E9)+"Put puzzle pieces together and discover a new story!"+getEmo(0x1F50D),
           "Solve new puzzles and have a relaxing time!"+getEmo(0x1F3A1)
   };

   private static String getEmo(int code) {
      char[] chars = Character.toChars(code);
      return new String(chars);
   }

   public static String[] inActive = {
           getEmo(0x2B50) + "Come back and play with fun!"+getEmo(0x2B50),
   };
   public static void main(String[] args) {
      for (String s : active) {
         System.out.println(s);
      }

      for (String s : inActive) {
         System.out.println(s);
      }
   }
}
