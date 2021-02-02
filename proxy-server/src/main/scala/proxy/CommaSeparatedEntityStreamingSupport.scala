package proxy

import akka.NotUsed
import akka.event.Logging
import akka.http.scaladsl.common.EntityStreamingSupport
import akka.http.scaladsl.model.{ContentType, ContentTypeRange, ContentTypes}
import akka.stream.scaladsl.{Flow, Framing}
import akka.util.ByteString
import akka.http.javadsl.{model => jm }

// inspired by CsvEntityStreamingSupport and JsonEntityStreamingSupport
final class CommaSeparatedEntityStreamingSupport (
                                                   maxLineLength:       Int,
                                                   val supported:       ContentTypeRange,
                                                   val contentType:     ContentType,
                                                   val framingRenderer: Flow[ByteString, ByteString, NotUsed],
                                                   val parallelism:     Int,
                                                   val unordered:       Boolean
                                                 ) extends EntityStreamingSupport {


  def this(maxObjectSize: Int) =
    this(
      maxObjectSize,
      ContentTypeRange(ContentTypes.`text/html(UTF-8)`),
      ContentTypes.`text/html(UTF-8)`,
      {
        val newline = ByteString()
        Flow[ByteString].intersperse(ByteString(""), ByteString(","), ByteString("."))
      },
      1, false)



  override val framingDecoder: Flow[ByteString, ByteString, NotUsed] =
    Framing.delimiter(ByteString(","), maxLineLength)

  def withFramingRendererFlow(framingRendererFlow: akka.stream.javadsl.Flow[ByteString, ByteString, NotUsed]): CommaSeparatedEntityStreamingSupport = {
    withFramingRenderer(framingRendererFlow.asScala)
  }
  def withFramingRenderer(framingRendererFlow: Flow[ByteString, ByteString, NotUsed]): CommaSeparatedEntityStreamingSupport =
    new CommaSeparatedEntityStreamingSupport(maxLineLength, supported, contentType, framingRendererFlow, parallelism, unordered)

  override def withContentType(ct: jm.ContentType): CommaSeparatedEntityStreamingSupport =
    new CommaSeparatedEntityStreamingSupport(maxLineLength, supported, ContentTypes.`text/html(UTF-8)`, framingRenderer, parallelism, unordered)

  override def withSupported(range: jm.ContentTypeRange): CommaSeparatedEntityStreamingSupport =
    new CommaSeparatedEntityStreamingSupport(maxLineLength, ContentTypeRange(ContentTypes.`text/html(UTF-8)`), contentType, framingRenderer, parallelism, unordered)

  override def withParallelMarshalling(parallelism: Int, unordered: Boolean): CommaSeparatedEntityStreamingSupport =
    new CommaSeparatedEntityStreamingSupport(maxLineLength, supported, contentType, framingRenderer, parallelism, unordered)

  override def toString = s"""${Logging.simpleName(getClass)}($maxLineLength, $supported, $contentType)"""
}
