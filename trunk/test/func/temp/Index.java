package temp;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.LockObtainFailedException;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.io.IOException;

public class Index {
    private final static String PATH = "G:/luceneTest/index/";


    public static void createIndex(String dir, List<Object[]> list, boolean create, boolean direct) {
        try {
            IndexWriter writer = new IndexWriter(dir, getAnalyzer(), create);
            for (int i = 0; i < list.size(); i++) {
                Document doc = new Document();
                Object[] obj = list.get(i);
                doc.add(new Field("id", obj[0].toString(), Field.Store.YES,
                        Field.Index.ANALYZED));
                doc.add(new Field("title", obj[1].toString(),
                        Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field("writeTime", obj[2].toString(),
                        Field.Store.YES, Field.Index.NO));
                doc.add(new Field("content", obj[3].toString(),
                        Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field("keyWord", obj[4].toString(),
                        Field.Store.YES, Field.Index.ANALYZED));
                if (create || direct)
                    writer.addDocument(doc);
                else {
                    if (isExist(dir, obj[0].toString())) {
                        deleteIndexByID(writer, obj[0].toString());
                        writer.addDocument(doc);
                    } else
                        writer.addDocument(doc);
                }
            }
            writer.optimize();
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * ���id�ж������Ƿ��Ѿ�����
     *
     * @param dir
     * @param id  Ψһ��ʶ
     * @return
     */
    public static boolean isExist(String dir, String id) {
        try {
            Searcher searcher = new IndexSearcher(dir);
            org.apache.lucene.search.Query query = new QueryParser("id", getAnalyzer()).parse(id);
            ScoreDoc[] docs = searcher.search(query, searcher.maxDoc()).scoreDocs;

            if (docs.length > 0)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static void deleteIndexByID(IndexWriter writer, String id) {
        org.apache.lucene.search.Query query = null;
        try {
            query = new QueryParser("id", getAnalyzer()).parse(id);
            writer.deleteDocuments(query);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Analyzer getAnalyzer() {
        return new StandardAnalyzer();
    }

//    public static void main(String[] args) throws CorruptIndexException, LockObtainFailedException,
//            IOException {
//
//        List<Object[]> list = new ArrayList<Object[]>();
//        Object[] obj01 = new Object[]{"1", "�й�", new Date(), "�������ݣ��Ұ��й�", "keyWord"};
//        list.add(obj01);
//        Object[] obj02 = new Object[]{"2", "�й�", new Date(), "�������ݣ������й��ˣ�", "keyWord"};
//        list.add(obj02);
//        Object[] obj03 = new Object[]{"3", "�9�", new Date(), "�9��й�", "keyWord"};
//        list.add(obj03);
//        Object[] obj04 = new Object[]{"4", "�9�", new Date(), "�й�Ҫ�����9�", "keyWord"};
//        list.add(obj04);
//        Object[] obj05 = new Object[]{"5", "������", new Date(), "�й�չ�ܿ�", "keyWord"};
//        list.add(obj05);
//        long startTime = new Date().getTime();
//        createIndex(PATH, list, true, true);
//        long endTime = new Date().getTime();
//        System.out.println("�⻨����" + (endTime - startTime) + " ����4��������!");
//
//        List<Object[]> listAdd = new ArrayList<Object[]>();
//        Object[] obj06 = new Object[]{"6", "�Ĵ����� �й�", new Date(), "��ϲ���й�", "keyWord"};
//        listAdd.add(obj06);
//        startTime = new Date().getTime();
//        createIndex(PATH, listAdd, false, true);
//        endTime = new Date().getTime();
//        System.out.println("�⻨����" + (endTime - startTime) + " ����4�������!");
//
//        List<Object[]> listModify = new ArrayList<Object[]>();
//        Object[] obj07 = new Object[]{"1", "�й�", new Date(), "����Ҹ��ˣ�����˵���й��ǿ��", "keyWord"};
//        listModify.add(obj07);
//        startTime = new Date().getTime();
//        createIndex(PATH, listModify, false, false);
//        endTime = new Date().getTime();
//        System.out.println("�⻨����" + (endTime - startTime) + " ����4�޸�����!");
//
//        startTime = new Date().getTime();
//        IndexWriter writer = new IndexWriter(PATH, getAnalyzer(), false);
//        deleteIndexByID(writer, "2");
//        writer.optimize();
//        writer.close();
//        endTime = new Date().getTime();
//        System.out.println("�⻨����" + (endTime - startTime) + " ����4ɾ������!");
//    }
}
