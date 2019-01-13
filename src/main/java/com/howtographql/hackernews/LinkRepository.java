package com.howtographql.hackernews;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Objects; // required for predicate Objects::nonnull

// The bson objects require explicit entry in POM as we're not
// using mongodb
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;




public class LinkRepository {
    
    private final List<Link> links;

    public LinkRepository() {
        links = new ArrayList<>();
        //add some links to start off with
        links.add(new Link("1", "http://howtographql.com", "Your favorite GraphQL page"));
        links.add(new Link("2", "http://graphql.org/learn/", "The official docks"));
        links.add(new Link("3", "https://github.com/howtographql/graphql-java", "Github"));
        links.add(new Link("4", "https://github.com/raj-julha/hackernews-graphql-java", "This sample's site"));
    }

   public List<Link> getAllLinks() {
       return links;
   }
    
    public List<Link> getAllLinks(LinkFilter filter, int skip, int first) {
        List<Link> resultList = new ArrayList<>();
        if(first == 2){
            resultList.add(links.get(0));
            resultList.add(links.get(1));
        }
        else {
            links.get(0).setDescription("updated desc to check filter");
            
            resultList.addAll(links);
        }

        if(filter != null){
            Predicate<Link> filterPredicate = getFilterPredicate2(filter);
            resultList = links.stream().filter(p -> filterPredicate.test(p)).collect(Collectors.toList());                                                
        }

        return resultList;
    }


    
    public void saveLink(Link link) {
        links.add(link);
    }

    private static Predicate<Link> getFilterPredicate(LinkFilter filter){
        Predicate<Link> condition = new Predicate<Link>(){
            @Override
            public boolean test(Link link){
                if(filter.getDescriptionContains() != null && filter.getDescriptionContains() != null){
                    return (link.getDescription().startsWith(filter.getDescriptionContains()) 
                        && link.getUrl().startsWith(filter.getUrlContains()));
                }
                return true;
            }
        };
        return condition;
    }

    /** 
    see sample from https://www.leveluplunch.com/java/tutorials/006-how-to-filter-arraylist-stream-java8/#creating-predicate
    The code in public void filter_by_team_to_collection()
    */
    private static Predicate<Link> getFilterPredicate2(LinkFilter filter){
        Predicate<Link> nonNullPredicate = Objects::nonNull;
        Predicate<Link> descriptionPredicate = null; 
        Predicate<Link> urlPredicate =  null;  
    
        if(filter.getDescriptionContains() != null && !filter.getDescriptionContains().isEmpty()){
            descriptionPredicate = p -> p.getDescription() != null && p.getDescription().startsWith(filter.getDescriptionContains());
        }

        if(filter.getUrlContains() != null && !filter.getUrlContains().isEmpty()){
            urlPredicate = p -> p.getUrl() != null && p.getUrl().startsWith(filter.getUrlContains());
        }

        Predicate<Link> fullPredicate = nonNullPredicate;
        if(descriptionPredicate != null){
            fullPredicate = fullPredicate.and(descriptionPredicate);
        }

        if(urlPredicate != null){
            fullPredicate = fullPredicate.and(urlPredicate);
        }
    
        return fullPredicate;
    }    


}