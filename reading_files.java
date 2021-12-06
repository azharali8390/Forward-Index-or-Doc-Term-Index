/*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
 */
package Assignment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import org.jsoup.Jsoup;
import org.apache.commons.lang3.StringUtils;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
//import org.tartarus.snowball.ext.PorterStemmer;
import java.util.HashSet;
import java.util.Map.Entry;
import static java.util.Map.entry;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import java.util.Scanner;
import static org.apache.commons.lang3.CharSetUtils.count;

/**
 *
 * @author Dell
 */
public class reading_files {

//    public void whenRemoveStopwordsUsingRemoveAll_thenSuccess(String stopwords,String a) 
//    {
//        ArrayList<String> allWords
//                = Stream.of(a)
//                        .collect(Collectors.toCollection(ArrayList<String>::new));
//        allWords.removeAll(stopwords);
//
//        String result = allWords.stream().collect(Collectors.joining(" "));
//        assertEquals(result, target);
//    }
    /////function for removing stopwords from string
    public String removeManually(String stopwords, String[] tokenized_file) {
        StringBuilder builder = new StringBuilder();
        for (String word : tokenized_file) {
            if (!stopwords.contains(word)) {
                builder.append(word);
                builder.append(' ');
            }
        }
        return builder.toString().trim();
    }
//////function for stemming of a sigle file the input paremeter is the text of a file

    public static String stem(String s) {
        SnowballStemmer snowballStemmer = new englishStemmer();
        StringBuilder builder = new StringBuilder();
        String[] spliting = s.split(" ");
        for (String word : spliting) {
            snowballStemmer.setCurrent(word);
            snowballStemmer.stem();
            String result = snowballStemmer.getCurrent();
            builder.append(result);
            builder.append(' ');
        }
        //String pp= builder.toString().trim();
        //System.out.println(pp);
        return builder.toString().trim();
    }
    /////making hashtable for one file
    public static Hashtable<Integer, String> hash(String stemmed_file,Hashtable<Integer, String> hash,int term_count,BufferedWriter term_ids/*Hashtable<Integer, String>for_one_func*/) throws IOException
    {
        //term_count++;
        String [] string=stemmed_file.split(" ");
        for(String word : string)
        {
             if(hash.containsValue(word)==false)
             {
                 term_count++;
                 //for_one_func.put(term_count,word);
                 hash.put(term_count, word);
                 String mix=(term_count)+"    "+word;
                 term_ids.append(mix);
                 //term_ids.append(word);
                 
                 term_ids.append("\n");
             }
        }
        System.out.println(term_count);
        return hash;
    }
    
    public static void forword_index(String stemmed_file,int doc_id,Hashtable<Integer, String> hash,BufferedWriter doc_index) throws IOException
    {
        int term_num = 1;
        int temp=0;
        String arr=doc_id+"    ";
        doc_index.append(arr);
        String[] words = stemmed_file.split(" ");
        int count = 0;
        Set<Entry<Integer, String>> entrySet
                = hash.entrySet();
        for (Entry<Integer, String> set: hash.entrySet()) 
        {
            String value = hash.get(term_num);
            //String key=set.getKey().toString();
            //hash.remove(set.getKey());
           
            for (String one_word : words) {
                if (value.equals(one_word)) {
                    count++;
                    temp=1;
                }
            }
             
            //doc_index.append(value);
            //doc_index.append("    ");
            if(temp==1)////for pushing only those words having count>0
            {
            String tt=term_num+":"+count+"    ";
            doc_index.append(tt);
            //doc_index.append(count+"    ");
            }
            temp=0;
            count=0;
            term_num++;
           
            

            // Printing all elements of a Map
            //System.out.println(set.getKey() + " = "+ set.getValue());
        }
        doc_index.append("\n");
        doc_index.append("\n");
        doc_index.append("\n");
        //int cc=0;
        //cc=0;
    }

    //////Function for converting all text in file to lower_case before a specific word
    public static String removeTillWord(String input, String word) {
        return input.substring(input.toLowerCase().indexOf(word));
    }

    @SuppressWarnings("fectching file from directry one by one will remove html tags and header")
    ////function will take the directry path as input
    public void read(final String directry_name) throws IOException {
//        String text = null;
//        String text1 = null;
        File directoryPath = new File(directry_name);
        File filesList[] = directoryPath.listFiles();
        int count = 0;
        String file_after_removing_headr = null;
        String file_after_rmoving_htmls = null;
        String substring = null;
        String token2 = "<!doctype";
        String token3 = "<html";
        String token = "<script";
        String file_name=" ";
        BufferedWriter doc_ids = new BufferedWriter(new FileWriter("docids.txt", true));
        BufferedWriter term_ids = new BufferedWriter(new FileWriter("termids.txt", true));
        BufferedWriter  doc_index = new BufferedWriter(new FileWriter(" doc_index.txt", true));
        Hashtable<Integer, String>hash_map = new Hashtable<Integer, String>();
      
        ArrayList<String> doc_after_removing_tags_header = new ArrayList<String>();
        int term_count=0;
        for (File file : filesList) {
            count++;

            String result = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
            result = result.toLowerCase();
            if (result.contains("<!doctype")) {

                file_after_removing_headr = result.contains(token2)
                        ? token2 + StringUtils.substringAfter(result, token2)
                        : result;
            } else if (result.contains("<html")) {
                result = result.toLowerCase();
                file_after_removing_headr = result.contains(token3)
                        ? token3 + StringUtils.substringAfter(result, token3)
                        : result;
            } else {
                result = result.toLowerCase();
                file_after_removing_headr = result.contains(token)
                        ? token + StringUtils.substringAfter(result, token)
                        : result;

            }

            file_after_rmoving_htmls = Jsoup.parse(file_after_removing_headr).text();
            String[] tokenized_file;

            // tokenizing the file and storing it in " Tokenized_file "  variable
            tokenized_file = file_after_rmoving_htmls.split("[\\p{Punct}\\s]+");

            //System.out.println(Arrays.toString(tokenized_file));
            /////converting 2d tokenized_file into string
            //String tokenized_string=Arrays.toString(tokenized_file);
            //System.out.println(tokenized_string);
            ///Fetching the list of stop words in program from directry
            String dir_path_of_stopwords = "D:\\Semester 7\\Information Retirievel\\stoplist.txt";
            String stop_words = new String(Files.readAllBytes(Paths.get(dir_path_of_stopwords)));
            //////function calling to remove stop words////uptill now stop words have been removed
            String file_after_rmovin_stopwords = removeManually(stop_words, tokenized_file);
            
            ///calling of function that track word to common word
            
            String stemmed_file = stem(file_after_rmovin_stopwords);
            //System.out.println(stemmed_file);
            
           
            //System.out.println(term_count);
            file_name=file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1)+"    "+count;
            System.out.println(file_name);
            doc_ids.append((file_name));
             doc_ids.append("\n");
             /////////////calling of hashing function
            //Hashtable<Integer, String>for_one_func = new Hashtable<Integer, String>();

             hash_map=hash(stemmed_file,hash_map,term_count,term_ids);////term_ids is the text file name
            term_count=hash_map.size();
            if (count==2) {
                int c = 0;
                c = 5;
            }
            forword_index(stemmed_file,count,hash_map,doc_index);
            //myWriter.write();
//            System.out.println(file_name);
//            
//            if(file_after_rmoving_htmlshas==null)
//            {
//                int c=0;
//                c=5;
//            }
//
//            System.out.println(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1));
//
//            //String p=tokenized_file[0].removeAll(stop_words);
             //System.out.println(doc_after_removing_tags_header.size()+"before");
            doc_after_removing_tags_header.add(file_after_rmoving_htmls);
            //System.out.println(doc_after_removing_tags_header.size()+" after");

            //System.out.println("File number is" + count);
        }
          
        int a = doc_after_removing_tags_header.size();
        doc_ids.close();
        term_ids.close();
        doc_index.close();
        System.out.println("file count"+a);
        

    }
}
