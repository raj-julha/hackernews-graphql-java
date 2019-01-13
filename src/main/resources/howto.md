## Implement Filtered Queries

### Schema Updates
1. Add a type for the filter
```json
input LinkFilter {
    description_contains: String
    url_contains: String
}
```
2. Update the Query type
```json
type Query {  
  allLinks(filter: LinkFilter, skip: Int = 0, first: Int = 0): [Link]
}
```

### Code Updates

1. Create the class LinkFilter containing properties on which we want to enable filter and that is defined in the schema type

1. Revise the allLinks method inside Query to accept a method seignatire as defined in the Query type
The entry in the schema
```
allLinks(filter: LinkFilter, skip: Int = 0, first: Int = 0): [Link]
```
becomes 
```java
public List<Link> allLinks(LinkFilter filter, Number skip, Number first) {
        return linkRepository.getAllLinks(filter, skip.intValue(), first.intValue());
}
```
3. Update LinkRepository.java to accept the new arguments and retrieve data accordingly. We have used Java8 Predicate to build the filter, see getFilterPredicate2 in LinkRepository.java


#### Query
```json
{
  allLinks {url
  description}
}

```
#### Result
```json
{
  "data": {
    "allLinks": [
      {
        "url": "http://howtographql.com",
        "description": "Your favorite GraphQL page"
      },
      {
        "url": "http://graphql.org/learn/",
        "description": "The official docks"
      },
      {
        "url": "https://github.com/howtographql/graphql-java",
        "description": "Github"
      }
    ]
  }
}
```

### Query
```json
{
  allLinks(filter: {description_contains: "The", url_contains: "http"}) {
    url
    description
  }
}

```

### Result
```json
{
  "data": {
    "allLinks": [
      {
        "url": "http://graphql.org/learn/",
        "description": "The official docks"
      }
    ]
  }
}

```

### Query
```json
{
  allLinks(filter: {url_contains: "http"}) {
    url
    description
  }
}
```

### Result
```json
{
  "data": {
    "allLinks": [
      {
        "url": "http://howtographql.com",
        "description": "updated desc to check filter"
      },
      {
        "url": "http://graphql.org/learn/",
        "description": "The official docks"
      },
      {
        "url": "https://github.com/howtographql/graphql-java",
        "description": "Github"
      }
    ]
  }
}
```

### Query
```json
{
  allLinks(filter: {url_contains: "https"}) {
    url
    description
  }
}
```

### Result
```json
{
  "data": {
    "allLinks": [
      {
        "url": "https://github.com/howtographql/graphql-java",
        "description": "Github"
      }
    ]
  }
}
```
