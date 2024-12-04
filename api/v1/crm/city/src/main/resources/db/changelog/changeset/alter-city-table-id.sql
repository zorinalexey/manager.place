ALTER TABLE public.cities
    ALTER id DROP DEFAULT,
ALTER id TYPE uuid using (gen_random_uuid()),
  ALTER id SET DEFAULT gen_random_uuid();