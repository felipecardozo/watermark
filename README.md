# H1 Watermark Service

WatermarkController is the class which provides the basic endpoints

to GET All Documents use "/" and GET Method

to GET A specific document use "/{ticket}" and GET Method, will return a Document

to Create a Document Use a Basic Json Object in the Body of the message, calling "/" and use POST Method
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

 the watermarkProperty is not required to create a document, the status in that case will be INPROGRESS

 to Update a Document Use a Basic Json Object in the Body of the message, calling /{ticket} and use PUT Method

 Here the user can finish the watermark of one document in case is INPROGRESS providing all the propeties.

 You can run the program with the jar provided in the root of the folder
java -jar watermark-0.0.1-SNAPSHOT.jar