SELECT * FROM Taxon;
SELECT taxon_id, node_rank, 'scientific name' FROM Taxon;
SELECT NCBI_Taxon_ID FROM Taxon t;
SELECT taxon_id FROM Taxon WHERE NCBI_Taxon_ID IS NOT NULL;
SELECT NCBI_Taxon_ID FROM Taxon t;
SELECT taxon_id FROM Taxon WHERE NCBI_Taxon_ID IS NOT NULL;
--SELECT SEQUENCE.nextval INTO :new.Oid FROM DUAL;
