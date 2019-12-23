package ru.tp_project.androidreader.model.xml


import org.simpleframework.xml.*

@Root(name = "FictionBook")
class BookXML {
    @get:Element(required=false, name = "stylesheet")
    @set:Element(required=false, name = "stylesheet")
    var stylesheet: Stylesheet = Stylesheet()

    class Stylesheet {
        @get:Attribute(required=false, name = "type")
        @set:Attribute(required=false, name = "type")
        var type: String = "-"
    }

    @get:Element(required=false, name = "description")
    @set:Element(required=false, name = "description")
    var description: Description = Description()

    class Description {
        @get:Element(required=false, name = "title-info")
        @set:Element(required=false, name = "title-info")
        var titleInfo: TitleInfo = TitleInfo()

        class TitleInfo {
            @get:Element(required=false, name = "genre")
            @set:Element(required=false, name = "genre")
            var genre: String = "-"

            @get:Element(required=false, name="author")
            @set:Element(required=false, name = "author")
            var author: Author = Author()

            class Author {
                @get:Element(required=false, name = "first-name")
                @set:Element(required=false, name = "first-name")
                var first_name: String = ""

                @get:Element(required=false, name = "last-name")
                @set:Element(required=false, name = "last-name")
                var last_name: String = ""
            }

            @get:Element(required=false, name="book-title")
            @set:Element(required=false, name = "book-title")
            var book_title: String = "-"

            @get:Element(required=false, name="lang")
            @set:Element(required=false, name = "lang")
            var lang: String = "-"

            @get:Element(required=false, name="date")
            @set:Element(required=false, name = "date")
            var date: String = "-"
        }

        @get:Element(required=false,name = "document-info")
        @set:Element(required=false,name = "document-info")
        var documentInfo: DocumentInfo = DocumentInfo()

        class DocumentInfo {
            @get:Element(required=false,name = "nickname")
            @set:Element(required=false,name = "nickname")
            var id: Author = Author()

            class Author {
                @get:Element(required=false,name = "nickname")
                @set:Element(required=false,name = "nickname")
                var nickname: String = ""
            }
            @get:Element(required=false,name = "date")
            @set:Element(required=false,name = "date")
            var date: String = ""

            @get:Element(required=false,name = "version")
            @set:Element(required=false,name = "version")
            var version: String = ""
        }

        @get:Element(required=false,name = "publish-info")
        @set:Element(required=false,name = "publish-info")
        var publishInfo: PublishInfo = PublishInfo()

        class PublishInfo {
            @get:Element(required=false,name = "publisher")
            @set:Element(required=false,name = "publisher")
            var publisher: String = ""
        }
    }

    @get:Element(required=false, name = "body")
    @set:Element(required=false, name = "body")
    var body: Body = Body()

    class Body {

        @get:Element(required=false, name = "title")
        @set:Element(required=false, name = "title")
        var title: P = P()


        class P {
            @get:Element(required=false, name = "p")
            @set:Element(required=false, name = "p")
            var p: String = "-"
        }

        @get:ElementList(required=false, name = "section", entry = "p", inline = true)
        @set:ElementList(required=false, name = "section", entry = "p", inline = true)
        var section: List<String> = listOf("")

        //@ElementList(required=false, name = "section",  inline = true)
        //@set:ElementList(required=false, name = "section",  inline = true)
        //var section: List<P>? = null

//        class Section {
//            @get:ElementList(required=false, name = "p")
//            @set:ElementList(required=false, name = "p")
//            var p: List<String>? = null
//        }
    }

    @get:Element(required=false, name = "binary")
    @set:Element(required=false, name = "binary")
    var binary: String = ""

    @get:Attribute(required=false,name = "id")
    @set:Attribute(required=false,name = "id")
    var idb: String = ""

    @get:Attribute(required=false,name = "content-type")
    @set:Attribute(required=false,name = "content-type")
    var content_type: String = ""
}

@Root(name = "section")
class SectionList {
    //@ElementList(requiSectionListred=false,  inline = true)
    //@field:ElementList(required=false,  entry = "p", inline = true)
    @set:ElementList(inline = true, required = false, entry = "p")
    @get:ElementList(inline = true, required = false, entry = "p")
    var section: List<String> = listOf("s")
}

@Root(name = "section")
class SectionList1 {
    //@ElementList(requiSectionListred=false,  inline = true)
    //@field:ElementList(required=false,  entry = "p", inline = true)
    @set:Element(required=false)
    @get:Element(required=false)
    var section: String = "section"
}


@Root(name = "p")
class P {
    @get:Element(required=false, name = "p")
    @set:Element(required=false, name = "p")
    var p: String = "-"
}
