import com.kw.common.dict.doublearraytrie.WordManager;
public class TmpTrieCheck {
    public static void main(String[] args) {
        WordManager manager = new WordManager();
        manager.init();
        String[] words = {"abs","ace","act","app","ant","hello","zzzznotfound"};
        for (String word : words) {
            System.out.println(word + "=" + manager.check(word));
        }
    }
}