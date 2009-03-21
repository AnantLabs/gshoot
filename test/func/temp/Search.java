package temp;

import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;

import java.util.ArrayList;
import java.util.List;

public class Search {


    private final static String PATH = "G:/luceneTest/index/";

    /**
     * ������
     *
     * @param dir       �����ļ�Ŀ¼
     * @param searchStr ��ѯ���
     * @param pageIndex ����ҳ
     * @param pageSize  ҳ��С
     * @return bject[0]:��¼���� �� Object[1]:���
     */
    public static List<Object> search(String dir, String searchStr,
                                      int pageIndex, int pageSize) {
        List<Object> result = new ArrayList<Object>();
        List<Object[]> resultList = new ArrayList<Object[]>();
        try {
            Searcher searcher = new IndexSearcher(dir);
            String whereStr = encapsulationCondition(searchStr);//���������װ

            org.apache.lucene.search.Query query = new QueryParser("title", getAnalyzer()).parse(whereStr);
            ScoreDoc[] docs = searcher.search(query, searcher.maxDoc()).scoreDocs;

            result.add(docs.length);//�������

            int beginIndex = (pageIndex - 1) * pageSize;//��¼�������

            int pageCount = docs.length / pageSize;
            if (docs.length % pageSize > 0)
                pageCount++;

            Document doc;
            for (int i = beginIndex; i < docs.length && i <= (beginIndex + pageSize); i++) {
                doc = searcher.doc(docs[i].doc);
                int maxLen = 200;//ֻ��ȡ���ݵ�ǰ200���ַ�
                if (doc.get("content").length() < maxLen)
                    maxLen = doc.get("content").length();
                resultList.add(new Object[]{doc.get("id"), doc.get("title"), doc.get("keyWord"), doc.get("writeTime"), doc.get("content").substring(0, maxLen)});
            }
            searcher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.add(resultList);

        return result;
    }

    /**
     * ���������װ
     *
     * @param keyWord
     * @return
     */
    public static String encapsulationCondition(String keyWord) {
        String whereStr = "title:" + keyWord;
        whereStr += " OR content:" + keyWord;
        whereStr += " OR keyWord:" + keyWord;
        return whereStr;
    }

    /**
     * ���������
     *
     * @return
     */
    public static Analyzer getAnalyzer() {
        return new StandardAnalyzer();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String str = "�й�";//��������
        List<Object> result = search(PATH, str, 1, 10);
        System.out.println("������ؽ��" + result.get(0) + "��\n");
        List<Object[]> resultList = (List<Object[]>) result.get(1);
        for (int i = 0; i < resultList.size(); i++) {
            System.out.print("id: " + resultList.get(i)[0]);
            System.out.print("\t title: " + resultList.get(i)[1]);
            System.out.println("\t content: " + resultList.get(i)[4]);
        }
        System.out.println("\n�������ж����У�" + str);
        System.out.println("�ǲ��Ǻ�ǿ�󰡣�");
    }
}
