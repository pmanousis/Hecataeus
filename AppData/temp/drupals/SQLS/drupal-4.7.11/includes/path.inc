SELECT COUNT(pid) FROM url_alias;
SELECT dst FROM url_alias WHERE src = '%s';
SELECT src FROM url_alias WHERE dst = '%s';
