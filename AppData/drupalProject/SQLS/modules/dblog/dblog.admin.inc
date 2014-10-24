SELECT w.*, u.name, u.uid FROM  watchdog  w ,  users  u WHERE w.uid = u.uid AND w.wid = 0;
