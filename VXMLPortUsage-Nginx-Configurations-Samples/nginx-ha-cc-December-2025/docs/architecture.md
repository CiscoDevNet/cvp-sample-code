
# Architecture

Nginx fronts Cloud Connect using a PRIMARY/BACKUP upstream model.
PRIMARY is always preferred; BACKUP is only used when PRIMARY is marked down.
