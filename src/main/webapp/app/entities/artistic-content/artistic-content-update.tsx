import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITag } from 'app/shared/model/tag.model';
import { getEntities as getTags } from 'app/entities/tag/tag.reducer';
import { IProject } from 'app/shared/model/project.model';
import { getEntities as getProjects } from 'app/entities/project/project.reducer';
import { IArtisticContent } from 'app/shared/model/artistic-content.model';
import { getEntity, updateEntity, createEntity, reset } from './artistic-content.reducer';

export const ArtisticContentUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const tags = useAppSelector(state => state.tag.entities);
  const projects = useAppSelector(state => state.project.entities);
  const artisticContentEntity = useAppSelector(state => state.artisticContent.entity);
  const loading = useAppSelector(state => state.artisticContent.loading);
  const updating = useAppSelector(state => state.artisticContent.updating);
  const updateSuccess = useAppSelector(state => state.artisticContent.updateSuccess);
  const handleClose = () => {
    props.history.push('/artistic-content');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getTags({}));
    dispatch(getProjects({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...artisticContentEntity,
      ...values,
      tags: mapIdList(values.tags),
      projects: mapIdList(values.projects),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...artisticContentEntity,
          tags: artisticContentEntity?.tags?.map(e => e.id.toString()),
          projects: artisticContentEntity?.projects?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="clapApplicationApp.artisticContent.home.createOrEditLabel" data-cy="ArtisticContentCreateUpdateHeading">
            <Translate contentKey="clapApplicationApp.artisticContent.home.createOrEditLabel">Create or edit a ArtisticContent</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="artistic-content-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('clapApplicationApp.artisticContent.title')}
                id="artistic-content-title"
                name="title"
                data-cy="title"
                type="text"
              />
              <ValidatedField
                label={translate('clapApplicationApp.artisticContent.description')}
                id="artistic-content-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('clapApplicationApp.artisticContent.contentUrl')}
                id="artistic-content-contentUrl"
                name="contentUrl"
                data-cy="contentUrl"
                type="text"
              />
              <ValidatedField
                label={translate('clapApplicationApp.artisticContent.uploadDate')}
                id="artistic-content-uploadDate"
                name="uploadDate"
                data-cy="uploadDate"
                type="date"
              />
              <ValidatedField
                label={translate('clapApplicationApp.artisticContent.viewCount')}
                id="artistic-content-viewCount"
                name="viewCount"
                data-cy="viewCount"
                type="text"
              />
              <ValidatedField
                label={translate('clapApplicationApp.artisticContent.tag')}
                id="artistic-content-tag"
                data-cy="tag"
                type="select"
                multiple
                name="tags"
              >
                <option value="" key="0" />
                {tags
                  ? tags.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('clapApplicationApp.artisticContent.project')}
                id="artistic-content-project"
                data-cy="project"
                type="select"
                multiple
                name="projects"
              >
                <option value="" key="0" />
                {projects
                  ? projects.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/artistic-content" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ArtisticContentUpdate;
