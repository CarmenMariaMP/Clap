import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/content-creator">
        <Translate contentKey="global.menu.entities.contentCreator" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/company">
        <Translate contentKey="global.menu.entities.company" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/artistic-content">
        <Translate contentKey="global.menu.entities.artisticContent" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/general">
        <Translate contentKey="global.menu.entities.general" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/photography">
        <Translate contentKey="global.menu.entities.photography" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/painting-or-sculpture">
        <Translate contentKey="global.menu.entities.paintingOrSculpture" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/music">
        <Translate contentKey="global.menu.entities.music" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/dance">
        <Translate contentKey="global.menu.entities.dance" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cinema">
        <Translate contentKey="global.menu.entities.cinema" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/tag">
        <Translate contentKey="global.menu.entities.tag" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/comment">
        <Translate contentKey="global.menu.entities.comment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/notification">
        <Translate contentKey="global.menu.entities.notification" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/notification-configuration">
        <Translate contentKey="global.menu.entities.notificationConfiguration" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/privacy-request">
        <Translate contentKey="global.menu.entities.privacyRequest" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/project">
        <Translate contentKey="global.menu.entities.project" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
