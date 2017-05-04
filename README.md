# H1 Watermark Service

WatermarkController is the class which provides the basic endpoints

# H2 to GET All Documents use "/" and GET Method

# H2 to GET A specific document use "/{ticket}" and GET Method, will return a Document

# H2 to Create a Document Use a Basic Json Object in the Body of the message, calling "/" and use POST Method
for example: 
{
    "title": "Here goes the Title",
    "author": "Here goes the Author",
    "watermarkProperty": {
        "topic": "Science||Business||Media",
        "content": "book||journal",
        "title": "Here goes the title",
        "author": "Here goes the author"
    }
}

# H4 the watermarkProperty is not required to create a document, the status in that case will be INPROGRESS

# H2 to Update a Document Use a Basic Json Object in the Body of the message, calling /{ticket} and use PUT Method

# H4 Here the user can finish the watermark of one document in case is INPROGRESS providing all the propeties.

# H3 You can run the program with the jar provided in the root of the folder
java -jar watermark-0.0.1-SNAPSHOT.jar