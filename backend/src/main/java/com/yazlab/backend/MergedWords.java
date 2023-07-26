package com.yazlab.backend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Document(collection = "words")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MergedWords {
    @Id
    private ObjectId id;
   private List<String> words;

   private String mergedWord="";
   double time=  0.0;


   public MergedWords(List<String> words,String mergedWord,double time){
       this.words=words;
       this.mergedWord=mergedWord;
       this.time=time;
   }

   public static String mainFunc(ArrayList<String> list){
       ArrayList<Listkeeper> x = new ArrayList<Listkeeper>();
       for (String s : list) {
           if(s.isEmpty())
               continue;
           Listkeeper st = new Listkeeper(s);
           x.add(st);
       }
       ArrayList<Character> charlist = new ArrayList<Character>();
       String lastword = x.get(0).a;
       String controller = "";
       x=find_most_similarity(x);
       if(x==null){
           return "Birleştirilemez";
       }

       for (int i = 0; i < x.size()-1; i++) {
           charlist = func1(lastword, x.get(i+1).a);
           controller="";
           for (int j = 0; j < charlist.size(); j++) {
               controller+=charlist.get(j);
           }
           if(controller.equals(lastword))
               System.out.println(x.get(i+1).a+" hatali");
           lastword="";
           for (int j = 0; j < charlist.size(); j++) {
               lastword+=charlist.get(j);
           }

       }

       return lastword;
   }

    public static ArrayList<Listkeeper> find_most_similarity(ArrayList<Listkeeper> x){
        String wordsOffirst=null;
        String wordsOfSecond=null;
        ArrayList<Integer> list = new ArrayList<Integer>();
        ArrayList<Integer> list2 = new ArrayList<Integer>();
        ArrayList<Integer> list3 = new ArrayList<Integer>();
        ArrayList<Integer> list4 = new ArrayList<Integer>();
        ArrayList<Integer> list5 = new ArrayList<Integer>();
        ArrayList<Integer> list6 = new ArrayList<Integer>();

        ArrayList<Listkeeper> sortlist = new ArrayList<Listkeeper>();

        int counter=0,saver=0;
        int index=-1,index2=-1;
        for (int i = 0; i < x.size(); i++) {
            wordsOffirst = x.get(i).a;
            for (int j = 0; j < x.size(); j++) {
                if(i==j)
                    continue;
                wordsOfSecond = x.get(j).a;
                for (int i2 = 0; i2 < wordsOffirst.length(); i2++) {
                    for (int j2 = 0; j2 < wordsOfSecond.length(); j2++) {
                        if(wordsOffirst.charAt(i2) == wordsOfSecond.charAt(j2)){
                            if(i2<wordsOffirst.length()-1 && j2<wordsOfSecond.length()-1){
                                if(wordsOfSecond.charAt(j2+1) == wordsOffirst.charAt(i2+1))
                                    i2++;
                            }
                            counter++;
                        }else if(counter!=0 || j2+1==wordsOfSecond.length()){
                            if(saver<counter){
                                saver=counter;
                                index=i2+1;
                                index2=j2;
                            }else if(counter>saver && j2+1==wordsOfSecond.length()){
                                saver=counter;
                                index=i2;
                                index2=j2;
                            }
                            counter=0;
                        }
                        if(counter>saver && j2+1==wordsOfSecond.length()){
                            saver=counter;
                            index=i2;
                            index2=j2;
                        }
                    }

                }
                if(saver>2||counter>1){
                    list.add(i);
                    list2.add(j);
                    list3.add(saver);
                    list5.add(index);
                    list6.add(index2);
                }
                index=-1;
                index2=-1;
                saver=0;
                counter=0;
            }
        }
        if(list.size()==0){
            return null;
        }


        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if(Objects.equals(list.get(i), list2.get(j)) && Objects.equals(list.get(j), list2.get(i))){
                    list.remove(j);
                    list2.remove(j);
                    list3.remove(j);
                    list5.remove(j);
                    list6.remove(j);
                }
            }
        }
        Listkeeper deneme=new Listkeeper();
        Listkeeper a=new Listkeeper(list,list2,list3,list5,list6);
        saver=-1;
        int max=0;
        int gecici=0;
        for (int i = 0; i < list3.size() && list.get(i) == 0; i++) {
            if(list3.get(i)>max){
                saver=i;
                max=list3.get(i);
            }else if(list3.get(i)==max){
                deneme = find_indis(x.get(list2.get(saver)).a,x.get(list2.get(i)).a);
                if(deneme.first.size()==x.get(list2.get(saver)).a.length()){
                    gecici=list2.get(saver);
                    x.get(list2.get(saver)).a = "";
                    a=remove_same_string(a, gecici);
                    saver=i-saver-1;
                    max=list3.get(i);
                    i--;
                }else if(deneme.second.size()==x.get(list2.get(i)).a.length()){
                    gecici=list2.get(i);
                    x.get(list2.get(i)).a = "";
                    a=remove_same_string(a, gecici);
                    saver=i-saver-1;
                    max=list3.get(i);
                    i--;
                }else{
                    if(x.get(list2.get(i)).a.length()<x.get(list2.get(saver)).a.length()){
                        saver=i;
                        max=list3.get(i);
                    }

                }

            }
        }
        list4.add(list.get(saver));
        list4.add(list2.get(saver));
        list=a.list;
        list2=a.list2;
        list3=a.list3;
        list5=a.list5;
        list6=a.list6;
        deneme=null;
        counter=0;
        index=-1;
        max=-1;
        index2=-1;
        for (int i = 0;i<list4.size(); i+=2) {
            for (int j = 0; j < list3.size(); j++) {
                counter=0;
                if(Objects.equals(list4.get(i + 1), list.get(j)) && !Objects.equals(list4.get(i), list2.get(j)) && list5.get(j)>list6.get(j) && list3.get(j)>=max){
                    counter=func(list4, list2, list, j);
                    if (counter==1)
                        continue;
                    max=list3.get(j);
                    index=j;
                    index2=0;
                }else if(Objects.equals(list4.get(i + 1), list2.get(j)) && !Objects.equals(list4.get(i), list.get(j)) && list5.get(j)<list6.get(j) && list3.get(j)>=max){
                    counter=func(list4, list2, list, j);
                    if (counter==1)
                        continue;
                    max=list3.get(j);
                    index=j;
                    index2=1;
                }else if(Objects.equals(list4.get(i + 1), list.get(j)) && !Objects.equals(list4.get(i), list2.get(j)) && Objects.equals(list5.get(j), list6.get(j)) && list3.get(j)>=max){
                    counter=func(list4, list2, list, j);
                    if (counter==1)
                        continue;
                    deneme = find_indis(x.get(list4.get(i+1)).a,x.get(list.get(j)).a);
                    if(x.get(list2.get(j)).a.length() == deneme.second.size()){
                        gecici=list2.get(j);
                        x.get(list2.get(j)).a = "";
                        a=remove_same_string(a, gecici);
                        j--;
                        continue;
                    }

                    max=list3.get(j);
                    index=j;
                    index2=0;
                }else if(Objects.equals(list4.get(i + 1), list2.get(j)) && !Objects.equals(list4.get(i), list.get(j)) && Objects.equals(list5.get(j), list6.get(j)) && list3.get(j)>=max){
                    counter=func(list4, list2, list, j);
                    if (counter==1)
                        continue;

                    deneme = find_indis(x.get(list4.get(i+1)).a,x.get(list.get(j)).a);
                    if(x.get(list.get(j)).a.length() == deneme.second.size()){
                        gecici=list.get(j);
                        x.get(list.get(j)).a = "";
                        a=remove_same_string(a, gecici);
                        j--;
                        System.out.println("as");
                        continue;
                    }

                    max=list3.get(j);
                    index=j;
                    index2=1;
                }
                list=a.list;
                list2=a.list2;
                list3=a.list3;
                list5=a.list5;
                list6=a.list6;
            }
            if(index2==1){
                list4.add(list2.get(index));
                list4.add(list.get(index));
            }else if(index2==0){
                list4.add(list.get(index));
                list4.add(list2.get(index));
            }

            index=-1;
            max=-1;
            index2=-1;
        }


        list4 = samelistobjectdelete(list4);

        for (int i = 0; i < list4.size(); i++) {
            sortlist.add(x.get(list4.get(i)));
        }
        if(list4.size()!=x.size()){
            Collections.sort(list4);
            for (int i = 0; i < list4.size(); i++) {
                if(list4.get(i)!=i)
                    System.out.println(x.get(i).a+" yazdirilamadi");
            }
        }

        return sortlist;
    }


    public static ArrayList<Integer> samelistobjectdelete(ArrayList<Integer> list4){
        for (int i = 0; i < list4.size(); i++) {
            for (int j = i+1; j < list4.size(); j++) {
                if(Objects.equals(list4.get(i), list4.get(j)))
                    list4.remove(j);
            }
        }
        return list4;
    }

    public static int func(ArrayList<Integer> list4,ArrayList<Integer> list2,ArrayList<Integer> list,int j){
        int counter=0;
        for (int k = 0; k < list4.size(); k++) {
            if(Objects.equals(list4.get(k), list.get(j)))
                counter++;
        }
        if(counter>1)
            return 1;
        counter=0;
        for (int k = 0; k < list4.size(); k++) {
            if(Objects.equals(list4.get(k), list2.get(j)))
                counter++;
        }
        if(counter>1)
            return 1;
        counter=0;
        return 0;
    }


    public static Listkeeper remove_same_string(Listkeeper x,int gecici){

        for (int j = 0; j < x.list.size(); j++) {
            if(x.list.get(j)==gecici){
                x.list.remove(j);
                x.list2.remove(j);
                x.list3.remove(j);
                x.list5.remove(j);
                x.list6.remove(j);
                j--;
            }
        }
        for (int j = 0; j < x.list2.size(); j++) {
            if(x.list2.get(j)==gecici){
                x.list.remove(j);
                x.list2.remove(j);
                x.list3.remove(j);
                x.list5.remove(j);
                x.list6.remove(j);
                j--;
            }
        }
        return x;
    }



    public static ArrayList<Character> func1(String lastword,String b){
        ArrayList<Integer> list = new ArrayList<Integer>();
        ArrayList<Integer> list2 = new ArrayList<Integer>();
        ArrayList<Integer> list3 = new ArrayList<Integer>();

        Listkeeper gecici=new Listkeeper();

        gecici = find_indis(lastword, b);
        list=gecici.first;
        list2= gecici.second;

        list3=find_max_size_index(list);

        ArrayList<Character> charlist = new ArrayList<Character>();

        if(!list3.isEmpty()){
            ArrayList<Integer> gecici_list = new ArrayList<Integer>();
            gecici_list=find_max_size_index(list);
            int index=list.get(gecici_list.get(1)-gecici_list.get(0)+1);
            int max = gecici_list.get(0);

            for (int i = 0; i < gecici_list.size(); i+=2) {
                if(gecici_list.get(i)>=max){
                    index=list.get(gecici_list.get(i+1));
                    max=gecici_list.get(i);
                }
            }

            for (int i = 0; i < index+1; i++) {
                charlist.add(lastword.charAt(i));
            }

        }else{
            for (int i=0; i <lastword.length(); i++) {
                charlist.add(lastword.charAt(i));
            }
            return charlist;
        }
        if(!list3.isEmpty()){
            int i;
            i = list2.get(list2.size()-1)+1;
            for (; i <b.length(); i++) {
                charlist.add(b.charAt(i));

            }
        }


        return charlist;
    }

    public static ArrayList<Integer> find_max_size_index(ArrayList<Integer> list2){
        int counter=1;
        ArrayList<Integer> list3 = new ArrayList<Integer>();
        for (int i = 0; i < list2.size(); i++) {
            int j=0;
            for ( j = i+1; j < list2.size(); ) {
                if(list2.get(i).equals(list2.get(j)-1)){
                    if(i+1<list2.size()){
                        i++;
                        j++;
                    }
                    counter++;
                }
                else{
                    break;
                }
            }
            if(counter>2){
                list3.add(counter);
                list3.add(i);
            }
            counter=1;
        }

        return list3;
    }

    public static Listkeeper find_indis(String a,String b) {
        int counter = 0;
        ArrayList<Integer> list = new ArrayList<Integer>();
        ArrayList<Integer> list2 = new ArrayList<Integer>();
        ArrayList<Integer> list4 = new ArrayList<Integer>();
        ArrayList<Integer> list5 = new ArrayList<Integer>();

        int saver = 0;

        for (int i = 0; i < a.length() - 1; i++) {
            for (int j = 0; j < b.length(); j++) {
                if (a.charAt(i) == b.charAt(j)) {
                    if (i + 1 < a.length() && j + 1 < b.length()) {
                        if (a.charAt(i + 1) == b.charAt(j + 1)) {
                            counter += 1;
                        } else {
                            saver = counter + 1;
                            i = i - counter;
                            counter = 0;
                            break;
                        }
                        if (counter >= 1) {
                            list4.add(i);
                            list5.add(j);
                            i++;
                        }
                    } else {
                        list4.add(i);
                        list5.add(j);
                        saver = counter + 1;//şuraya bak
                        counter = 0;
                        break;
                    }


                }
            }
            if (counter > 1 || saver > 2) {
                for (int j = 0; j < list4.size(); j++) {
                    list.add(list4.get(j));
                }
                for (int j = 0; j < list5.size(); j++) {
                    list2.add(list5.get(j));
                }
                /*if(saver>2 && counter==0){
                    list.add(list4.get(list4.size()-1)+1);
                    list2.add(list5.get(list5.size()-1)+1);
                }*/
            } else {
                list4.clear();
                list5.clear();
            }
            saver = 0;
            counter = 0;
        }
        Listkeeper returnlist = new Listkeeper(list, list2);
        return returnlist;
    }

}
