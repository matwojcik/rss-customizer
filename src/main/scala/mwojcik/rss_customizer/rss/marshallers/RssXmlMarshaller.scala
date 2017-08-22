package mwojcik.rss_customizer.rss.marshallers

import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport
import akka.http.scaladsl.marshalling.ToEntityMarshaller
import akka.http.scaladsl.model.{ContentType, HttpCharsets, MediaTypes}

import scala.xml.NodeSeq

/**
  * Gives ability to Marshall scala.xml.Node to HttpEntity with setting media type
  * application/rss+xml
  */
trait RssXmlMarshaller extends ScalaXmlSupport{

  implicit def RssXmlMarshaller: ToEntityMarshaller[NodeSeq] =
    defaultNodeSeqMarshaller.map { response =>
      response.withContentType(
        ContentType(MediaTypes.`application/rss+xml`, charset = HttpCharsets.`UTF-8`)
      )
    }

}
