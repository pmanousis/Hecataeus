SELECT taxon_id, left_value, right_value FROM '.$taxontbl.' WHERE parent_taxon_id = ? ORDER BY ncbi_taxon_id;
