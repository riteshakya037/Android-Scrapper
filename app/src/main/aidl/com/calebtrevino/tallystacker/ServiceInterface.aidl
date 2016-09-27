// ChildGameEventListener.aidl
package com.calebtrevino.tallystacker;

import com.calebtrevino.tallystacker.ServiceListener;

interface ServiceInterface {
 void addListener(ServiceListener listener);

  void removeListener(ServiceListener listener);}

