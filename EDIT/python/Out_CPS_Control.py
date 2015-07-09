"""
Adds the host to OBJECTNAME

"""
OBJECTNAME = "/NetObjects/Host_Trigger_Object/Out_CPS_Control"

class Trigger(HostTrigger):
    def trigger(self):
        obj = self.pldb.object_get(OBJECTNAME)
        if obj is None:
            print "Couldn't find object '%s'" % OBJECTNAME
            return
        #self.pld.dyn_add(obj.id, self.ip)

    def reset(self):
        obj = self.pldb.object_get(OBJECTNAME)
        if obj is None:
            return
        self.pld.dyn_remove(obj.id, self.ip)
