#include <libnotify/notify.h>
#include <string>
#include "notification.h"

void show_notify(char* text)
{
	notify_init("Messager");

	NotifyNotification* n =
		notify_notification_new("From phone", text, "dialog-information");

	notify_notification_show(n, nullptr);

	g_object_unref(G_OBJECT(n));
	notify_uninit();
	return;
}
