/*
 * Copyright 2005-2006 Jerome LOUVEL
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.txt
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * http://www.opensource.org/licenses/cddl1.txt
 * If applicable, add the following below this CDDL
 * HEADER, with the fields enclosed by brackets "[]"
 * replaced with your own identifying information:
 * Portions Copyright [yyyy] [name of copyright owner]
 */

package org.restlet;

import org.restlet.component.Component;

/**
 * Default Maplet that can directly be used.
 * @see <a href="http://www.restlet.org/tutorial#part11">Tutorial: Maplets and hierarchical URIs</a>
 */
public class DefaultMaplet extends AbstractRestlet implements Maplet
{
   /** Delegate Maplet actually implementing the Maplet methods. */
   protected Maplet delegate;

   /**
    * Creates a new Maplet in the given container.
    * @param parent The parent component.
    */
   public DefaultMaplet(Component parent)
   {
      super(parent);
      this.delegate = Factory.getInstance().createMaplet(parent);
   }

   /**
    * Attaches a target instance shared by all calls.
    * @param pathPattern The path pattern used to map calls.
    * @param target The target instance to attach.
    * @see java.util.regex.Pattern
    */
   public void attach(String pathPattern, Restlet target)
   {
      delegate.attach(pathPattern, target);
   }

   /**
    * Attaches a target class. A new instance will be created for each call.
    * @param pathPattern The path pattern used to map calls.
    * @param targetClass The target class to attach (can have a constructor taking a RestletContainer
    * parameter).
    * @see java.util.regex.Pattern
    */
   public void attach(String pathPattern, Class<? extends Restlet> targetClass)
   {
      delegate.attach(pathPattern, targetClass);
   }

   /**
    * Detaches a target instance.
    * @param target The target instance to detach.
    */
   public void detach(Restlet target)
   {
      delegate.detach(target);
   }

   /**
    * Detaches a target class.
    * @param targetClass The target class to detach.
    */
   public void detach(Class<? extends Restlet> targetClass)
   {
      delegate.detach(targetClass);
   }

   /**
    * Handles a call to a resource or a set of resources. 
    * Default behavior to be overriden: delegation to one of the attached Restlets.
    * @param call The call to handle.
    */
   public void handle(Call call)
   {
      delegate.handle(call);
   }

   /**
    * Delegates a call to one of the attached targets.<br/>
    * If no delegation is possible, a 404 error status (Client error, Not found) will be returned.
    * @param call The call to delegate.
    * @return True if the call was successfully delegated.
    */
   public boolean delegate(Call call)
   {
      return delegate.delegate(call);
   }

}
