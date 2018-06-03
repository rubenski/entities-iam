CREATE TABLE public.f_logged_out_token (
  token character varying(800) NOT NULL,
  logout_time timestamp without time zone,
  expiry_time timestamp without time zone
);


ALTER TABLE public.f_logged_out_token OWNER TO faceter;

--
-- Name: f_logged_out_token f_logged_out_token_pkey; Type: CONSTRAINT; Schema: public; Owner: faceter
--

ALTER TABLE ONLY public.f_logged_out_token
  ADD CONSTRAINT f_logged_out_token_pkey PRIMARY KEY (token);


--
-- Name: index_logged_out_token; Type: INDEX; Schema: public; Owner: faceter
--

CREATE INDEX index_logged_out_token ON public.f_logged_out_token USING btree (token);
