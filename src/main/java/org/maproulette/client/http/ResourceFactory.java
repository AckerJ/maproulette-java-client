package org.maproulette.client.http;

import java.net.URI;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.maproulette.client.exception.MapRouletteRuntimeException;

/**
 * Factory for retrieving resource based on method. Set system property 'org.maproulette.http_proxy'
 * to use an HTTP proxy for fetching resources.
 *
 * @author mcuthbert
 */
public class ResourceFactory
{
    private String proxy;

    public ResourceFactory()
    {
        // no class variables required for class
    }

    public ResourceFactory withProxy(final String proxy)
    {
        this.proxy = proxy;
        return this;
    }

    public HttpResource resource(final String methodName, final String uri)
    {
        return this.resource(methodName, URI.create(uri));
    }

    public HttpResource resource(final String methodName, final URI uri)
    {
        final HttpResource resource;
        switch (methodName)
        {
            case HttpGet.METHOD_NAME:
                resource = new GetResource(uri);
                break;
            case HttpDelete.METHOD_NAME:
                resource = new DeleteResource(uri);
                break;
            case HttpPost.METHOD_NAME:
                resource = new PostResource(uri);
                break;
            case HttpPut.METHOD_NAME:
                resource = new PutResource(uri);
                break;
            default:
                throw new MapRouletteRuntimeException(
                        String.format("Invalid method name %s provided", methodName));
        }
        if (StringUtils.isNotEmpty(this.proxy))
        {
            resource.setProxy(HttpHost.create(this.proxy));
        }
        return resource;
    }
}
